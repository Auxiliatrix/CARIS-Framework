package lavaplayer.container.ogg;

import static lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.tools.FriendlyException;
import lavaplayer.tools.io.SeekableInputStream;
import lavaplayer.track.AudioTrackInfo;
import lavaplayer.track.BaseAudioTrack;
import lavaplayer.track.playback.AudioProcessingContext;
import lavaplayer.track.playback.LocalAudioTrackExecutor;

/**
 * Audio track which handles an OGG stream.
 */
public class OggAudioTrack extends BaseAudioTrack {
  private static final Logger log = LoggerFactory.getLogger(OggAudioTrack.class);

  private final SeekableInputStream inputStream;

  /**
   * @param trackInfo Track info
   * @param inputStream Input stream for the OGG stream
   */
  public OggAudioTrack(AudioTrackInfo trackInfo, SeekableInputStream inputStream) {
    super(trackInfo);

    this.inputStream = inputStream;
  }

  @Override
  public void process(final LocalAudioTrackExecutor localExecutor) throws Exception {
    OggPacketInputStream packetInputStream = new OggPacketInputStream(inputStream);

    log.debug("Starting to play an OGG stream track {}", getIdentifier());

    localExecutor.executeProcessingLoop(() -> {
      try {
        processTrackLoop(packetInputStream, localExecutor.getProcessingContext());
      } catch (IOException e) {
        throw new FriendlyException("Stream broke when playing OGG track.", SUSPICIOUS, e);
      }
    }, null);
  }

  private void processTrackLoop(OggPacketInputStream packetInputStream, AudioProcessingContext context) throws IOException, InterruptedException {
    OggTrackProvider track = OggTrackLoader.loadTrack(packetInputStream);

    if (track == null) {
      throw new IOException("Stream terminated before the first packet.");
    }

    while (track != null) {
      try {
        track.initialise(context);
        track.provideFrames();
      } finally {
        track.close();
      }

      track = OggTrackLoader.loadTrack(packetInputStream);
    }
  }
}
