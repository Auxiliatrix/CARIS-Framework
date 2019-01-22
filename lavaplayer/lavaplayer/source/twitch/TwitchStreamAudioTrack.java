package lavaplayer.source.twitch;

import static lavaplayer.source.twitch.TwitchStreamAudioSourceManager.getChannelIdentifierFromUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.source.AudioSourceManager;
import lavaplayer.source.stream.M3uStreamAudioTrack;
import lavaplayer.source.stream.M3uStreamSegmentUrlProvider;
import lavaplayer.tools.io.HttpInterface;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;
import lavaplayer.track.playback.LocalAudioTrackExecutor;

/**
 * Audio track that handles processing Twitch tracks.
 */
public class TwitchStreamAudioTrack extends M3uStreamAudioTrack {
  private static final Logger log = LoggerFactory.getLogger(TwitchStreamAudioTrack.class);

  private final TwitchStreamAudioSourceManager sourceManager;
  private final String channelName;

  /**
   * @param trackInfo Track info
   * @param sourceManager Source manager which was used to find this track
   */
  public TwitchStreamAudioTrack(AudioTrackInfo trackInfo, TwitchStreamAudioSourceManager sourceManager) {
    super(trackInfo);

    this.sourceManager = sourceManager;
    this.channelName = getChannelIdentifierFromUrl(trackInfo.identifier);
  }

  @Override
  protected M3uStreamSegmentUrlProvider createSegmentProvider() {
    return new TwitchStreamSegmentUrlProvider(getChannelIdentifierFromUrl(trackInfo.identifier));
  }

  @Override
  protected HttpInterface getHttpInterface() {
    return sourceManager.getHttpInterface();
  }

  @Override
  public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
    log.debug("Starting to play Twitch channel {}.", channelName);

    super.process(localExecutor);
  }

  @Override
  public AudioTrack makeClone() {
    return new TwitchStreamAudioTrack(trackInfo, sourceManager);
  }

  @Override
  public AudioSourceManager getSourceManager() {
    return sourceManager;
  }
}
