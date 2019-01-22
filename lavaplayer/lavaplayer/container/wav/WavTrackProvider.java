package lavaplayer.container.wav;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import lavaplayer.filter.FilterChainBuilder;
import lavaplayer.filter.ShortPcmAudioFilter;
import lavaplayer.tools.io.SeekableInputStream;
import lavaplayer.track.playback.AudioProcessingContext;

/**
 * A provider of audio frames from a WAV track.
 */
public class WavTrackProvider {
  private static final int BLOCKS_IN_BUFFER = 4096;

  private final SeekableInputStream inputStream;
  private final DataInput dataInput;
  private final WavFileInfo info;
  private final ShortPcmAudioFilter downstream;
  private final short[] buffer;
  private final ShortBuffer nioBuffer;
  private final byte[] rawBuffer;

  /**
   * @param context Configuration and output information for processing
   * @param inputStream Input stream to use
   * @param info Information about the WAV file
   */
  public WavTrackProvider(AudioProcessingContext context, SeekableInputStream inputStream, WavFileInfo info) {
    this.inputStream = inputStream;
    this.dataInput = new DataInputStream(inputStream);
    this.info = info;
    this.downstream = FilterChainBuilder.forShortPcm(context, info.channelCount, info.sampleRate, true);
    this.buffer = info.getPadding() > 0 ? new short[info.channelCount * BLOCKS_IN_BUFFER] : null;

    ByteBuffer byteBuffer = ByteBuffer.allocate(info.blockAlign * BLOCKS_IN_BUFFER).order(LITTLE_ENDIAN);
    this.rawBuffer = byteBuffer.array();
    this.nioBuffer = byteBuffer.asShortBuffer();
  }

  /**
   * Seeks to the specified timecode.
   * @param timecode The timecode in milliseconds
   */
  public void seekToTimecode(long timecode) {
    try {
      long fileOffset = (timecode * info.sampleRate / 1000L) * info.blockAlign + info.startOffset;
      inputStream.seek(fileOffset);
      downstream.seekPerformed(timecode, timecode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Reads audio frames and sends them to frame consumer
   * @throws InterruptedException When interrupted
   */
  public void provideFrames() throws InterruptedException {
    try {
      int blockCount;

      while ((blockCount = getNextChunkBlocks()) > 0) {
        if (buffer != null) {
          processChunkWithPadding(blockCount);
        } else {
          processChunk(blockCount);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Free all resources associated to processing the track.
   */
  public void close() {
    downstream.close();
  }

  private void processChunkWithPadding(int blockCount) throws IOException, InterruptedException {
    readChunkToBuffer(blockCount);

    int padding = info.getPadding() / 2;
    int sampleCount = blockCount * info.channelCount;
    int indexInBlock = 0;

    for (int i = 0; i < sampleCount; i++) {
      buffer[i] = nioBuffer.get();

      if (++indexInBlock == info.channelCount) {
        nioBuffer.position(nioBuffer.position() + padding);
        indexInBlock = 0;
      }
    }

    downstream.process(buffer, 0, blockCount * info.channelCount);
  }

  private void processChunk(int blockCount) throws IOException, InterruptedException {
    readChunkToBuffer(blockCount);
    downstream.process(nioBuffer);
  }

  private void readChunkToBuffer(int blockCount) throws IOException {
    int bytesToRead = blockCount * info.blockAlign;
    dataInput.readFully(rawBuffer, 0, bytesToRead);

    nioBuffer.position(0);
    nioBuffer.limit(bytesToRead / 2);
  }

  private int getNextChunkBlocks() {
    long endOffset = info.startOffset + info.blockAlign * info.blockCount;
    return (int) Math.min((endOffset - inputStream.getPosition()) / info.blockAlign, BLOCKS_IN_BUFFER);
  }
}
