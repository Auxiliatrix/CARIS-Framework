package lavaplayer.player.event;

import lavaplayer.player.AudioPlayer;

/**
 * Event that is fired when a player is paused.
 */
public class PlayerPauseEvent extends AudioEvent {
  /**
   * @param player Audio player
   */
  public PlayerPauseEvent(AudioPlayer player) {
    super(player);
  }
}
