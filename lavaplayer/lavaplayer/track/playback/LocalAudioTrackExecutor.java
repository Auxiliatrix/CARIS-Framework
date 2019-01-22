package lavaplayer.track.playback;

import static lavaplayer.tools.ExceptionTools.findDeepException;
import static lavaplayer.tools.FriendlyException.Severity.FAULT;
import static lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;
import static lavaplayer.track.TrackMarkerHandler.MarkerState.ENDED;
import static lavaplayer.track.TrackMarkerHandler.MarkerState.STOPPED;

import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.format.AudioDataFormat;
import lavaplayer.player.AudioConfiguration;
import lavaplayer.tools.ExceptionTools;
import lavaplayer.tools.FriendlyException;
import lavaplayer.track.AudioTrackState;
import lavaplayer.track.InternalAudioTrack;
import lavaplayer.track.TrackMarker;
import lavaplayer.track.TrackMarkerTracker;
import lavaplayer.track.TrackStateListener;

/**
 * Handles the execution and output buffering of an audio track.
 */
public class LocalAudioTrackExecutor implements AudioTrackExecutor {
  private static final Logger log = LoggerFactory.getLogger(LocalAudioTrackExecutor.class);

  private final InternalAudioTrack audioTrack;
  private final AudioProcessingContext processingContext;
  private final boolean useSeekGhosting;
  private final AudioFrameBuffer frameBuffer;
  private final AtomicReference<Thread> playingThread = new AtomicReference<>();
  private final AtomicBoolean isStopping = new AtomicBoolean(false);
  private final AtomicLong pendingSeek = new AtomicLong(-1);
  private final AtomicLong lastFrameTimecode = new AtomicLong(0);
  private final AtomicReference<AudioTrackState> state = new AtomicReference<>(AudioTrackState.INACTIVE);
  private final Object actionSynchronizer = new Object();
  private final TrackMarkerTracker markerTracker = new TrackMarkerTracker();
  private boolean interruptibleForSeek = false;
  private volatile Throwable trackException;

  /**
   * @param audioTrack The audio track that this executor executes
   * @param configuration Configuration to use for audio processing
   * @param volumeLevel Mutable volume level to use when executing the track
   * @param useSeekGhosting Whether to keep providing old frames continuing from the previous position during a seek
   *                        until frames from the new position arrive.
   * @param bufferDuration The size of the frame buffer in milliseconds
   */
  public LocalAudioTrackExecutor(InternalAudioTrack audioTrack, AudioConfiguration configuration, AtomicInteger volumeLevel,
                                 boolean useSeekGhosting, int bufferDuration) {

    this.audioTrack = audioTrack;
    AudioDataFormat currentFormat = configuration.getOutputFormat();
    this.frameBuffer = new AudioFrameBuffer(bufferDuration, currentFormat, isStopping);
    this.processingContext = new AudioProcessingContext(configuration, frameBuffer, volumeLevel, currentFormat);
    this.useSeekGhosting = useSeekGhosting;
  }

  public AudioProcessingContext getProcessingContext() {
    return processingContext;
  }

  @Override
  public AudioFrameBuffer getAudioBuffer() {
    return frameBuffer;
  }

  @Override
  public void execute(TrackStateListener listener) {
    InterruptedException interrupt = null;

    if (Thread.interrupted()) {
      log.debug("Cleared a stray interrupt.");
    }

    if (playingThread.compareAndSet(null, Thread.currentThread())) {
      log.debug("Starting to play track {} locally with listener {}", audioTrack.getInfo().identifier, listener);

      state.set(AudioTrackState.LOADING);

      try {
        audioTrack.process(this);

        log.debug("Playing track {} finished or was stopped.", audioTrack.getIdentifier());
      } catch (Throwable e) {
        // Temporarily clear the interrupted status so it would not disrupt listener methods.
        interrupt = findInterrupt(e);

        if (interrupt != null && checkStopped()) {
          log.debug("Track {} was interrupted outside of execution loop.", audioTrack.getIdentifier());
        } else {
          frameBuffer.setTerminateOnEmpty();

          FriendlyException exception = ExceptionTools.wrapUnfriendlyExceptions("Something broke when playing the track.", FAULT, e);
          ExceptionTools.log(log, exception, "playback of " + audioTrack.getIdentifier());

          trackException = exception;
          listener.onTrackException(audioTrack, exception);

          ExceptionTools.rethrowErrors(e);
        }
      } finally {
        synchronized (actionSynchronizer) {
          interrupt = interrupt != null ? interrupt : findInterrupt(null);

          playingThread.compareAndSet(Thread.currentThread(), null);

          markerTracker.trigger(ENDED);
          state.set(AudioTrackState.FINISHED);
        }

        if (interrupt != null) {
          Thread.currentThread().interrupt();
        }
      }
    } else {
      log.warn("Tried to start an already playing track {}", audioTrack.getIdentifier());
    }
  }

  @Override
  public void stop() {
    synchronized (actionSynchronizer) {
      Thread thread = playingThread.get();

      if (thread != null) {
        log.debug("Requesting stop for track {}", audioTrack.getIdentifier());

        isStopping.compareAndSet(false, true);
        thread.interrupt();
      } else {
        log.debug("Tried to stop track {} which is not playing.", audioTrack.getIdentifier());
      }
    }
  }

  /**
   * @return True if the track has been scheduled to stop and then clears the scheduled stop bit.
   */
  public boolean checkStopped() {
    return isStopping.compareAndSet(true, false);
  }

  /**
   * Wait until all the frames from the frame buffer have been consumed. Keeps the buffering thread alive to keep it
   * interruptible for seeking until buffer is empty.
   */
  public void waitOnEnd() throws InterruptedException {
    frameBuffer.setTerminateOnEmpty();
    frameBuffer.waitForTermination();
  }

  /**
   * Interrupt the buffering thread, either stop or seek should have been set beforehand.
   * @return True if there was a thread to interrupt.
   */
  public boolean interrupt() {
    synchronized (actionSynchronizer) {
      Thread thread = playingThread.get();

      if (thread != null) {
        thread.interrupt();
        return true;
      }

      return false;
    }
  }

  @Override
  public long getPosition() {
    long seek = pendingSeek.get();
    return seek != -1 ? seek : lastFrameTimecode.get();
  }

  @Override
  public void setPosition(long timecode) {
    if (!audioTrack.isSeekable()) {
      return;
    }

    synchronized (actionSynchronizer) {
      if (timecode < 0) {
        timecode = 0;
      }

      pendingSeek.set(timecode);

      if (!useSeekGhosting) {
        frameBuffer.clear();
      }

      interruptForSeek();
    }
  }

  @Override
  public AudioTrackState getState() {
    return state.get();
  }

  /**
   * @return True if this track is currently in the middle of a seek.
   */
  private boolean isPerformingSeek() {
    return pendingSeek.get() != -1 || (useSeekGhosting && frameBuffer.hasClearOnInsert());
  }

  @Override
  public void setMarker(TrackMarker marker) {
    markerTracker.set(marker, getPosition());
  }

  @Override
  public boolean failedBeforeLoad() {
    return trackException != null && !frameBuffer.hasReceivedFrames();
  }

  /**
   * Execute the read and seek loop for the track.
   * @param readExecutor Callback for reading the track
   * @param seekExecutor Callback for performing a seek on the track, may be null on a non-seekable track
   */
  public void executeProcessingLoop(ReadExecutor readExecutor, SeekExecutor seekExecutor) {
    boolean proceed = true;

    checkPendingSeek(seekExecutor);

    while (proceed) {
      state.set(AudioTrackState.PLAYING);
      proceed = false;

      try {
        // An interrupt may have been placed while we were handling the previous one.
        if (Thread.interrupted() && !handlePlaybackInterrupt(null, seekExecutor)) {
          break;
        }

        setInterruptibleForSeek(true);
        readExecutor.performRead();
        setInterruptibleForSeek(false);

        // Must not finish before terminator frame has been consumed the user may still want to perform seeks until then
        waitOnEnd();
      } catch (Exception e) {
        setInterruptibleForSeek(false);
        InterruptedException interruption = findInterrupt(e);

        if (interruption != null) {
          proceed = handlePlaybackInterrupt(interruption, seekExecutor);
        } else {
          throw ExceptionTools.wrapUnfriendlyExceptions("Something went wrong when decoding the track.", FAULT, e);
        }
      }
    }
  }

  private void setInterruptibleForSeek(boolean state) {
    synchronized (actionSynchronizer) {
      interruptibleForSeek = state;
    }
  }

  private void interruptForSeek() {
    boolean interrupted = false;

    synchronized (actionSynchronizer) {
      if (interruptibleForSeek) {
        interruptibleForSeek = false;
        Thread thread = playingThread.get();

        if (thread != null) {
          thread.interrupt();
          interrupted = true;
        }
      }
    }

    if (interrupted) {
      log.debug("Interrupting playing thread to perform a seek {}", audioTrack.getIdentifier());
    } else {
      log.debug("Seeking on track {} while not in playback loop.", audioTrack.getIdentifier());
    }
  }

  private boolean handlePlaybackInterrupt(InterruptedException interruption, SeekExecutor seekExecutor) {
    Thread.interrupted();

    if (checkStopped()) {
      markerTracker.trigger(STOPPED);
      return false;
    } else if (checkPendingSeek(seekExecutor)) {
      // Double-check, might have received a stop request while seeking
      if (checkStopped()) {
        markerTracker.trigger(STOPPED);
        return false;
      } else {
        return true;
      }
    } else if (interruption != null) {
      Thread.currentThread().interrupt();
      throw new FriendlyException("The track was unexpectedly terminated.", SUSPICIOUS, interruption);
    } else {
      return true;
    }
  }

  private InterruptedException findInterrupt(Throwable throwable) {
    InterruptedException exception = findDeepException(throwable, InterruptedException.class);

    if (exception == null) {
      InterruptedIOException ioException = findDeepException(throwable, InterruptedIOException.class);

      if (ioException != null && (ioException.getMessage() == null || !ioException.getMessage().contains("timed out"))) {
        exception = new InterruptedException(ioException.getMessage());
      }
    }

    if (exception == null && Thread.interrupted()) {
      return new InterruptedException();
    }

    return exception;
  }

  /**
   * Performs a seek if it scheduled.
   * @param seekExecutor Callback for performing a seek on the track
   * @return True if a seek was performed
   */
  private boolean checkPendingSeek(SeekExecutor seekExecutor) {
    if (!audioTrack.isSeekable()) {
      return false;
    }

    long seekPosition;

    synchronized (actionSynchronizer) {
      seekPosition = pendingSeek.get();

      if (seekPosition == -1) {
        return false;
      }

      log.debug("Track {} interrupted for seeking to {}.", audioTrack.getIdentifier(), seekPosition);
      applySeekState(seekPosition);
    }

    try {
      seekExecutor.performSeek(seekPosition);
    } catch (Exception e) {
      throw ExceptionTools.wrapUnfriendlyExceptions("Something went wrong when seeking to a position.", FAULT, e);
    }

    return true;
  }

  private void applySeekState(long seekPosition) {
    state.set(AudioTrackState.SEEKING);

    if (useSeekGhosting) {
      frameBuffer.setClearOnInsert();
    } else {
      frameBuffer.clear();
    }

    pendingSeek.set(-1);
    markerTracker.checkSeekTimecode(seekPosition);
  }

  @Override
  public AudioFrame provide() {
    AudioFrame frame = frameBuffer.provide();
    processProvidedFrame(frame);
    return frame;
  }

  @Override
  public AudioFrame provide(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
    AudioFrame frame = frameBuffer.provide(timeout, unit);
    processProvidedFrame(frame);
    return frame;
  }

  private void processProvidedFrame(AudioFrame frame) {
    if (frame != null && !frame.isTerminator()) {
      if (!isPerformingSeek()) {
        markerTracker.checkPlaybackTimecode(frame.timecode);
      }

      lastFrameTimecode.set(frame.timecode);
    }
  }

  /**
   * Read executor, see method description
   */
  public interface ReadExecutor {
    /**
     * Reads until interrupted or EOF
     * @throws InterruptedException
     */
    void performRead() throws InterruptedException;
  }

  /**
   * Seek executor, see method description
   */
  public interface SeekExecutor {
    /**
     * Perform a seek to the specified position
     * @param position Position in milliseconds
     */
    void performSeek(long position);
  }
}
