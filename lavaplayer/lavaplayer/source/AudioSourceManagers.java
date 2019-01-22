package lavaplayer.source;

import lavaplayer.player.AudioPlayerManager;
import lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import lavaplayer.source.beam.BeamAudioSourceManager;
import lavaplayer.source.http.HttpAudioSourceManager;
import lavaplayer.source.local.LocalAudioSourceManager;
import lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import lavaplayer.source.vimeo.VimeoAudioSourceManager;
import lavaplayer.source.youtube.YoutubeAudioSourceManager;

/**
 * A helper class for registering built-in source managers to a player manager.
 */
public class AudioSourceManagers {
  /**
   * Registers all built-in remote audio sources to the specified player manager. Local file audio source must be
   * registered separately.
   *
   * @param playerManager Player manager to register the source managers to
   */
  public static void registerRemoteSources(AudioPlayerManager playerManager) {
    playerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
    playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
    playerManager.registerSourceManager(new BandcampAudioSourceManager());
    playerManager.registerSourceManager(new VimeoAudioSourceManager());
    playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
    playerManager.registerSourceManager(new BeamAudioSourceManager());
    playerManager.registerSourceManager(new HttpAudioSourceManager());
  }

  /**
   * Registers the local file source manager to the specified player manager.
   *
   * @param playerManager Player manager to register the source manager to
   */
  public static void registerLocalSource(AudioPlayerManager playerManager) {
    playerManager.registerSourceManager(new LocalAudioSourceManager());
  }
}
