package lavaplayer.format.transcoder;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import lavaplayer.format.AudioDataFormat;
import lavaplayer.natives.opus.OpusEncoder;
import lavaplayer.player.AudioConfiguration;

/**
 * Audio chunk encoder for Opus codec.
 */
public class OpusChunkEncoder implements AudioChunkEncoder {
  private final AudioDataFormat format;
  private final OpusEncoder encoder;
  private final ByteBuffer encodedBuffer;

  /**
   * @param configuration Audio configuration used for configuring the encoder
   * @param format Target audio format.
   */
  public OpusChunkEncoder(AudioConfiguration configuration, AudioDataFormat format) {
    encodedBuffer = ByteBuffer.allocateDirect(4096);
    encoder = new OpusEncoder(format.sampleRate, format.channelCount, configuration.getOpusEncodingQuality());
    this.format = format;
  }

  @Override
  public byte[] encode(ShortBuffer buffer) {
    encoder.encode(buffer, format.chunkSampleCount, encodedBuffer);

    byte[] bytes = new byte[encodedBuffer.remaining()];
    encodedBuffer.get(bytes);
    return bytes;
  }

  @Override
  public void close() {
    encoder.close();
  }
}
