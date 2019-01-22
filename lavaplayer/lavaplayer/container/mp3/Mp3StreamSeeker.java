package lavaplayer.container.mp3;

import java.io.IOException;

import lavaplayer.tools.io.SeekableInputStream;

/**
 * Seeker for an MP3 stream, which actually does not allow seeking and reports Long.MAX_VALUE as duration.
 */
public class Mp3StreamSeeker implements Mp3Seeker {
  @Override
  public long getDuration() {
    return Long.MAX_VALUE;
  }

  @Override
  public boolean isSeekable() {
    return false;
  }

  @Override
  public long seekAndGetFrameIndex(long timecode, SeekableInputStream inputStream) throws IOException {
    throw new UnsupportedOperationException("Cannot seek on a stream.");
  }
}
