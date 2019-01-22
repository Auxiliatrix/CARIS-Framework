package lavaplayer.track;

import lavaplayer.player.AudioPlayerManager;
import lavaplayer.track.playback.AudioFrameProvider;
import lavaplayer.track.playback.AudioTrackExecutor;
import lavaplayer.track.playback.LocalAudioTrackExecutor;

/**
 * Methods of an audio track that should not be visible outside of the library
 */
public interface InternalAudioTrack extends AudioTrack, AudioFrameProvider {
  /**
   * @param executor Executor to assign to the track
   * @param applyPrimordialState True if the state previously applied to this track should be copied to new executor.
   */
  void assignExecutor(AudioTrackExecutor executor, boolean applyPrimordialState);

  /**
   * @return Get the active track executor
   */
  AudioTrackExecutor getActiveExecutor();

  /**
   * Perform any necessary loading and then enter the read/seek loop
   * @param executor The local executor which processes this track
   */
  void process(LocalAudioTrackExecutor executor) throws Exception;

  /**
   * @param playerManager The player manager which is executing this track
   * @return A custom local executor for this track. Unless this track requires a special executor, this should return
   *         null as the default one will be used in that case.
   */
  AudioTrackExecutor createLocalExecutor(AudioPlayerManager playerManager);
}
