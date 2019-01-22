package lavaplayer.source.soundcloud;

import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.container.mp3.Mp3AudioTrack;
import lavaplayer.source.AudioSourceManager;
import lavaplayer.tools.io.HttpInterface;
import lavaplayer.tools.io.PersistentHttpStream;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;
import lavaplayer.track.DelegatedAudioTrack;
import lavaplayer.track.playback.LocalAudioTrackExecutor;

/**
 * Audio track that handles processing SoundCloud tracks.
 */
public class SoundCloudAudioTrack extends DelegatedAudioTrack {
  private static final Logger log = LoggerFactory.getLogger(SoundCloudAudioTrack.class);

  private final SoundCloudAudioSourceManager sourceManager;

  /**
   * @param trackInfo Track info
   * @param sourceManager Source manager which was used to find this track
   */
  public SoundCloudAudioTrack(AudioTrackInfo trackInfo, SoundCloudAudioSourceManager sourceManager) {
    super(trackInfo);

    this.sourceManager = sourceManager;
  }

  @Override
  public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
    try (HttpInterface httpInterface = sourceManager.getHttpInterface()) {
      if (!attemptLoadStream(localExecutor, httpInterface, true)) {
        sourceManager.updateClientId();

        attemptLoadStream(localExecutor, httpInterface, false);
      }
    }
  }

  private boolean attemptLoadStream(LocalAudioTrackExecutor localExecutor, HttpInterface httpInterface, boolean checkUnauthorized) throws Exception {
    String trackUrl = sourceManager.getTrackUrlFromId(trackInfo.identifier);
    log.debug("Starting SoundCloud track from URL: {}", trackUrl);

    try (PersistentHttpStream stream = new PersistentHttpStream(httpInterface, new URI(trackUrl), null)) {
      if (checkUnauthorized) {
        int statusCode = stream.checkStatusCode();

        if (statusCode == 401) {
          return false;
        } else if (statusCode < 200 && statusCode >= 300) {
          throw new IOException("Invalid status code for soundcloud stream: " + statusCode);
        }
      }

      processDelegate(new Mp3AudioTrack(trackInfo, stream), localExecutor);
    }

    return true;
  }

  @Override
  public AudioTrack makeClone() {
    return new SoundCloudAudioTrack(trackInfo, sourceManager);
  }

  @Override
  public AudioSourceManager getSourceManager() {
    return sourceManager;
  }
}
