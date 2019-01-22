package lavaplayer.container.adts;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.track.AudioTrackInfo;
import lavaplayer.track.BaseAudioTrack;
import lavaplayer.track.playback.LocalAudioTrackExecutor;

/**
 * Audio track that handles an ADTS packet stream
 */
public class AdtsAudioTrack extends BaseAudioTrack {
  private static final Logger log = LoggerFactory.getLogger(AdtsAudioTrack.class);

  private final InputStream inputStream;

  /**
   * @param trackInfo Track info
   * @param inputStream Input stream for the ADTS stream
   */
  public AdtsAudioTrack(AudioTrackInfo trackInfo, InputStream inputStream) {
    super(trackInfo);

    this.inputStream = inputStream;
  }

  @Override
  public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
    AdtsStreamProvider provider = new AdtsStreamProvider(inputStream, localExecutor.getProcessingContext());

    try {
      log.debug("Starting to play ADTS stream {}", getIdentifier());

      localExecutor.executeProcessingLoop(provider::provideFrames, null);
    } finally {
      provider.close();
    }
  }
}
