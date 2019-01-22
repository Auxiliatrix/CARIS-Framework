package lavaplayer.container.ogg;

import java.io.IOException;

import lavaplayer.track.playback.AudioProcessingContext;

/**
 * A handler for a specific codec for an OGG stream.
 */
public interface OggTrackProvider {
  /**
   * Initialises the track stream.
   * @param context Configuration and output information for processing audio
   */
  void initialise(AudioProcessingContext context) throws IOException;

  /**
   * Decodes audio frames and sends them to frame consumer
   * @throws InterruptedException
   */
  void provideFrames() throws InterruptedException;

  /**
   * Seeks to the specified timecode.
   * @param timecode The timecode in milliseconds
   */
  void seekToTimecode(long timecode);

  /**
   * Free all resources associated to processing the track.
   */
  void close();
}
