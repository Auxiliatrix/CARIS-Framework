package lavaplayer.player.event;

import lavaplayer.player.AudioPlayer;

/**
 * Event that is fired when a player is resumed.
 */
public class PlayerResumeEvent extends AudioEvent {
  /**
   * @param player Audio player
   */
  public PlayerResumeEvent(AudioPlayer player) {
    super(player);
  }
}
