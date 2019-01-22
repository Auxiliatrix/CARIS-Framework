package lavaplayer.container.flac;

import java.io.IOException;

import lavaplayer.container.flac.frame.FlacFrameReader;
import lavaplayer.filter.FilterChainBuilder;
import lavaplayer.filter.SplitShortPcmAudioFilter;
import lavaplayer.tools.io.BitStreamReader;
import lavaplayer.tools.io.SeekableInputStream;
import lavaplayer.track.playback.AudioProcessingContext;

/**
 * A provider of audio frames from a FLAC track.
 */
public class FlacTrackProvider {
  private final FlacTrackInfo info;
  private final SeekableInputStream inputStream;
  private final SplitShortPcmAudioFilter downstream;
  private final BitStreamReader bitStreamReader;
  private final int[] decodingBuffer;
  private final int[][] rawSampleBuffers;
  private final short[][] sampleBuffers;

  /**
   * @param context Configuration and output information for processing
   * @param info Track information from FLAC metadata
   * @param inputStream Input stream to use
   */
  public FlacTrackProvider(AudioProcessingContext context, FlacTrackInfo info, SeekableInputStream inputStream) {
    this.info = info;
    this.inputStream = inputStream;
    this.downstream = FilterChainBuilder.forSplitShortPcm(context, info.stream.sampleRate);
    this.bitStreamReader = new BitStreamReader(inputStream);
    this.decodingBuffer = new int[FlacFrameReader.TEMPORARY_BUFFER_SIZE];
    this.rawSampleBuffers = new int[info.stream.channelCount][];
    this.sampleBuffers = new short[info.stream.channelCount][];

    for (int i = 0; i < rawSampleBuffers.length; i++) {
      rawSampleBuffers[i] = new int[info.stream.maximumBlockSize];
      sampleBuffers[i] = new short[info.stream.maximumBlockSize];
    }
  }

  /**
   * Decodes audio frames and sends them to frame consumer
   * @throws InterruptedException
   */
  public void provideFrames() throws InterruptedException {
    try {
      int sampleCount;

      while ((sampleCount = readFlacFrame()) != 0) {
        downstream.process(sampleBuffers, 0, sampleCount);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private int readFlacFrame() throws IOException {
    return FlacFrameReader.readFlacFrame(inputStream, bitStreamReader, info.stream, rawSampleBuffers, sampleBuffers, decodingBuffer);
  }

  /**
   * Seeks to the specified timecode.
   * @param timecode The timecode in milliseconds
   */
  public void seekToTimecode(long timecode) {
    try {
      FlacSeekPoint seekPoint = findSeekPointForTime(timecode);
      inputStream.seek(info.firstFramePosition + seekPoint.byteOffset);
      downstream.seekPerformed(timecode, seekPoint.sampleIndex * 1000 / info.stream.sampleRate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private FlacSeekPoint findSeekPointForTime(long timecode) {
    if (info.seekPointCount == 0) {
      return new FlacSeekPoint(0, 0, 0);
    }

    long targetSampleIndex = timecode * info.stream.sampleRate / 1000L;
    return binarySearchSeekPoints(info.seekPoints, info.seekPointCount, targetSampleIndex);
  }

  private FlacSeekPoint binarySearchSeekPoints(FlacSeekPoint[] seekPoints, int length, long targetSampleIndex) {
    int low = 0;
    int high = length - 1;

    while (high > low) {
      int mid = (low + high + 1) / 2;

      if (info.seekPoints[mid].sampleIndex > targetSampleIndex) {
        high = mid - 1;
      } else {
        low = mid;
      }
    }

    return seekPoints[low];
  }

  /**
   * Free all resources associated to processing the track.
   */
  public void close() {
    downstream.close();
  }
}
