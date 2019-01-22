package lavaplayer.player;

import static lavaplayer.track.AudioTrackEndReason.CLEANUP;
import static lavaplayer.track.AudioTrackEndReason.FINISHED;
import static lavaplayer.track.AudioTrackEndReason.LOAD_FAILED;
import static lavaplayer.track.AudioTrackEndReason.REPLACED;
import static lavaplayer.track.AudioTrackEndReason.STOPPED;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.player.event.AudioEvent;
import lavaplayer.player.event.AudioEventListener;
import lavaplayer.player.event.PlayerPauseEvent;
import lavaplayer.player.event.PlayerResumeEvent;
import lavaplayer.player.event.TrackEndEvent;
import lavaplayer.player.event.TrackExceptionEvent;
import lavaplayer.player.event.TrackStartEvent;
import lavaplayer.player.event.TrackStuckEvent;
import lavaplayer.player.hook.AudioOutputHook;
import lavaplayer.tools.FriendlyException;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackEndReason;
import lavaplayer.track.InternalAudioTrack;
import lavaplayer.track.TrackStateListener;
import lavaplayer.track.playback.AudioFrame;
import lavaplayer.track.playback.AudioFrameProvider;
import lavaplayer.track.playback.AudioFrameProviderTools;

/**
 * An audio player that is capable of playing audio tracks and provides audio frames from the currently playing track.
 */
public class AudioPlayer implements AudioFrameProvider, TrackStateListener {
  private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class);

  private volatile InternalAudioTrack activeTrack;
  private volatile long lastRequestTime;
  private volatile long lastReceiveTime;
  private volatile boolean stuckEventSent;
  private volatile InternalAudioTrack shadowTrack;
  private final AtomicBoolean paused;
  private final DefaultAudioPlayerManager manager;
  private final List<AudioEventListener> listeners;
  private final AtomicInteger volumeLevel;
  private final AudioOutputHook outputHook;
  private final Object trackSwitchLock;

  /**
   * @param manager Audio player manager which this player is attached to
   * @param outputHook Hook which can intercept outgoing audio frames
   */
  public AudioPlayer(DefaultAudioPlayerManager manager, AudioOutputHook outputHook) {
    this.manager = manager;
    this.outputHook = outputHook;
    activeTrack = null;
    paused = new AtomicBoolean();
    listeners = new ArrayList<>();
    volumeLevel = new AtomicInteger(100);
    trackSwitchLock = new Object();
  }

  /**
   * @return Currently playing track
   */
  public AudioTrack getPlayingTrack() {
    return activeTrack;
  }

  /**
   * @param track The track to start playing
   */
  public void playTrack(AudioTrack track) {
    startTrack(track, false);
  }

  /**
   * @param track The track to start playing, passing null will stop the current track and return false
   * @param noInterrupt Whether to only start if nothing else is playing
   * @return True if the track was started
   */
  public boolean startTrack(AudioTrack track, boolean noInterrupt) {
    InternalAudioTrack newTrack = (InternalAudioTrack) track;
    InternalAudioTrack previousTrack;

    synchronized (trackSwitchLock) {
      previousTrack = activeTrack;

      if (noInterrupt && previousTrack != null) {
        return false;
      }

      activeTrack = newTrack;
      lastRequestTime = System.currentTimeMillis();
      lastReceiveTime = System.nanoTime();
      stuckEventSent = false;

      if (previousTrack != null) {
        previousTrack.stop();
        dispatchEvent(new TrackEndEvent(this, previousTrack, newTrack == null ? STOPPED : REPLACED));

        shadowTrack = previousTrack;
      }
    }

    if (newTrack == null) {
      shadowTrack = null;
      return false;
    }

    dispatchEvent(new TrackStartEvent(this, newTrack));

    manager.executeTrack(this, newTrack, manager.getConfiguration(), volumeLevel);
    return true;
  }

  /**
   * Stop currently playing track.
   */
  public void stopTrack() {
    stopWithReason(STOPPED);
  }

  private void stopWithReason(AudioTrackEndReason reason) {
    shadowTrack = null;

    synchronized (trackSwitchLock) {
      InternalAudioTrack previousTrack = activeTrack;
      activeTrack = null;

      if (previousTrack != null) {
        previousTrack.stop();
        dispatchEvent(new TrackEndEvent(this, previousTrack, reason));
      }
    }
  }

  private AudioFrame provideShadowFrame() {
    InternalAudioTrack shadow = shadowTrack;
    AudioFrame frame = null;

    if (shadow != null) {
      frame = shadow.provide();

      if (frame != null && frame.isTerminator()) {
        shadowTrack = null;
        frame = null;
      }
    }

    return frame;
  }

  @Override
  public AudioFrame provide() {
    return AudioFrameProviderTools.delegateToTimedProvide(this);
  }

  @Override
  public AudioFrame provide(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
    AudioFrame frame = provideDirectly(timeout, unit);
    if (outputHook != null) {
      frame = outputHook.outgoingFrame(this, frame);
    }
    return frame;
  }

  /**
   * Provide an audio frame bypassing hooks.
   * @param timeout Specifies the maximum time to wait for data. Pass 0 for non-blocking mode.
   * @param unit Specifies the time unit of the maximum wait time.
   * @return An audio frame if available, otherwise null
   */
  public AudioFrame provideDirectly(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
    InternalAudioTrack track;

    lastRequestTime = System.currentTimeMillis();

    if (timeout == 0 && paused.get()) {
      return null;
    }

    while ((track = activeTrack) != null) {
      AudioFrame frame = timeout > 0 ? track.provide(timeout, unit) : track.provide();

      if (frame != null) {
        lastReceiveTime = System.nanoTime();
        shadowTrack = null;

        if (frame.isTerminator()) {
          handleTerminator(track);
          continue;
        }
      } else if (timeout == 0) {
        checkStuck(track);

        frame = provideShadowFrame();
      }

      return frame;
    }

    return null;
  }

  private void handleTerminator(InternalAudioTrack track) {
    synchronized (trackSwitchLock) {
      if (activeTrack == track) {
        activeTrack = null;

        dispatchEvent(new TrackEndEvent(this, track, track.getActiveExecutor().failedBeforeLoad() ? LOAD_FAILED : FINISHED));
      }
    }
  }

  private void checkStuck(AudioTrack track) {
    if (!stuckEventSent && System.nanoTime() - lastReceiveTime > manager.getTrackStuckThresholdNanos()) {
      stuckEventSent = true;
      dispatchEvent(new TrackStuckEvent(this, track, TimeUnit.NANOSECONDS.toMillis(manager.getTrackStuckThresholdNanos())));
    }
  }

  public int getVolume() {
    return volumeLevel.get();
  }

  public void setVolume(int volume) {
    volumeLevel.set(Math.min(150, Math.max(0, volume)));
  }

  /**
   * @return Whether the player is paused
   */
  public boolean isPaused() {
    return paused.get();
  }

  /**
   * @param value True to pause, false to resume
   */
  public void setPaused(boolean value) {
    if (paused.compareAndSet(!value, value)) {
      if (value) {
        dispatchEvent(new PlayerPauseEvent(this));
      } else {
        dispatchEvent(new PlayerResumeEvent(this));
        lastReceiveTime = System.nanoTime();
      }
    }
  }

  /**
   * Destroy the player and stop playing track.
   */
  public void destroy() {
    stopTrack();
  }

  /**
   * Add a listener to events from this player.
   * @param listener New listener
   */
  public void addListener(AudioEventListener listener) {
    synchronized (trackSwitchLock) {
      listeners.add(listener);
    }
  }

  /**
   * Remove an attached listener using identity comparison.
   * @param listener The listener to remove
   */
  public void removeListener(AudioEventListener listener) {
    synchronized (trackSwitchLock) {
      for (Iterator<AudioEventListener> iterator = listeners.iterator(); iterator.hasNext(); ) {
        if (iterator.next() == listener) {
          iterator.remove();
        }
      }
    }
  }

  private void dispatchEvent(AudioEvent event) {
    log.debug("Firing an event with class {}", event.getClass().getSimpleName());

    synchronized (trackSwitchLock) {
      for (AudioEventListener listener : listeners) {
        try {
          listener.onEvent(event);
        } catch (Exception e) {
          log.error("Handler of event {} threw an exception.", event, e);
        }
      }
    }
  }

  @Override
  public void onTrackException(AudioTrack track, FriendlyException exception) {
    dispatchEvent(new TrackExceptionEvent(this, track, exception));
  }

  @Override
  public void onTrackStuck(AudioTrack track, long thresholdMs) {
    dispatchEvent(new TrackStuckEvent(this, track, thresholdMs));
  }

  /**
   * Check if the player should be "cleaned up" - stopped due to nothing using it, with the given threshold.
   * @param threshold Threshold in milliseconds to use
   */
  public void checkCleanup(long threshold) {
    AudioTrack track = getPlayingTrack();
    if (track != null && System.currentTimeMillis() - lastRequestTime >= threshold) {
      log.debug("Triggering cleanup on an audio player playing track {}", track);

      stopWithReason(CLEANUP);
    }
  }
}
