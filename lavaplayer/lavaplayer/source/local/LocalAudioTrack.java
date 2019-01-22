package lavaplayer.source.local;

import java.io.File;

import lavaplayer.container.MediaContainerProbe;
import lavaplayer.source.AudioSourceManager;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;
import lavaplayer.track.DelegatedAudioTrack;
import lavaplayer.track.InternalAudioTrack;
import lavaplayer.track.playback.LocalAudioTrackExecutor;

/**
 * Audio track that handles processing local files as audio tracks.
 */
public class LocalAudioTrack extends DelegatedAudioTrack {
  private final File file;
  private final MediaContainerProbe probe;
  private final LocalAudioSourceManager sourceManager;

  /**
   * @param trackInfo Track info
   * @param probe Probe for the media container of this track
   * @param sourceManager Source manager used to load this track
   */
  public LocalAudioTrack(AudioTrackInfo trackInfo, MediaContainerProbe probe, LocalAudioSourceManager sourceManager) {
    super(trackInfo);

    this.file = new File(trackInfo.identifier);
    this.probe = probe;
    this.sourceManager = sourceManager;
  }

  /**
   * @return The media probe which handles creating a container-specific delegated track for this track.
   */
  public MediaContainerProbe getProbe() {
    return probe;
  }

  @Override
  public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
    try (LocalSeekableInputStream inputStream = new LocalSeekableInputStream(file)) {
      processDelegate((InternalAudioTrack) probe.createTrack(trackInfo, inputStream), localExecutor);
    }
  }

  @Override
  public AudioTrack makeClone() {
    return new LocalAudioTrack(trackInfo, probe, sourceManager);
  }

  @Override
  public AudioSourceManager getSourceManager() {
    return sourceManager;
  }
}