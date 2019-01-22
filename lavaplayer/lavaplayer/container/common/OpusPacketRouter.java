package lavaplayer.container.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.filter.FilterChainBuilder;
import lavaplayer.filter.ShortPcmAudioFilter;
import lavaplayer.filter.volume.AudioFrameVolumeChanger;
import lavaplayer.format.AudioDataFormat;
import lavaplayer.natives.opus.OpusDecoder;
import lavaplayer.track.playback.AudioFrame;
import lavaplayer.track.playback.AudioProcessingContext;

/**
 * A router for opus packets to the output specified by an audio processing context. It automatically detects if the
 * packets can go clean through to the output without any decoding and encoding steps on each packet and rebuilds the
 * pipeline of the output if necessary.
 */
public class OpusPacketRouter {
  private static final Logger log = LoggerFactory.getLogger(OpusPacketRouter.class);

  private final AudioProcessingContext context;
  private final int inputFrequency;
  private final int inputChannels;
  private final byte[] headerBytes;

  private long currentTimecode;
  private OpusDecoder opusDecoder;
  private ShortPcmAudioFilter downstream;
  private ByteBuffer directInput;
  private ShortBuffer frameBuffer;
  private AudioDataFormat inputFormat;
  private int lastFrameSize;

  /**
   * @param context Configuration and output information for processing audio
   * @param inputFrequency Sample rate of the opus track
   * @param inputChannels Number of channels in the opus track
   */
  public OpusPacketRouter(AudioProcessingContext context, int inputFrequency, int inputChannels) {
    this.context = context;
    this.inputFrequency = inputFrequency;
    this.inputChannels = inputChannels;
    this.headerBytes = new byte[2];
    this.lastFrameSize = 0;
  }

  /**
   * Notify downstream handlers about a seek.
   *
   * @param requestedTimecode Timecode in milliseconds to which the seek was requested to
   * @param providedTimecode Timecode in milliseconds to which the seek was actually performed to
   */
  public void seekPerformed(long requestedTimecode, long providedTimecode) {
    currentTimecode = providedTimecode;

    if (downstream != null) {
      downstream.seekPerformed(requestedTimecode, providedTimecode);
    }
  }

  /**
   * Indicates that no more input is coming. Flush any buffers to output.
   * @throws InterruptedException
   */
  public void flush() throws InterruptedException {
    if (downstream != null) {
      downstream.flush();
    }
  }

  /**
   * Process one opus packet.
   * @param buffer Byte buffer of the packet
   * @throws InterruptedException
   */
  public void process(ByteBuffer buffer) throws InterruptedException {
    int frameSize = processFrameSize(buffer);

    if (frameSize != 0) {
      checkDecoderNecessity();

      if (opusDecoder != null) {
        passDownstream(buffer, frameSize);
      } else {
        passThrough(buffer);
      }
    }
  }

  /**
   * Free all resources.
   */
  public void close() {
    if (opusDecoder != null) {
      destroyDecoder();
    }
  }

  private int processFrameSize(ByteBuffer buffer) {
    int frameSize;

    if (buffer.isDirect()) {
      buffer.mark();
      buffer.get(headerBytes);
      buffer.reset();

      frameSize = OpusDecoder.getPacketFrameSize(inputFrequency, headerBytes, 0, headerBytes.length);
    } else {
      frameSize = OpusDecoder.getPacketFrameSize(inputFrequency, buffer.array(), buffer.position(), buffer.remaining());
    }

    if (frameSize == 0) {
      return 0;
    } else if (frameSize != lastFrameSize) {
      lastFrameSize = frameSize;
      inputFormat = new AudioDataFormat(inputChannels, inputFrequency, frameSize, AudioDataFormat.Codec.OPUS);
    }

    currentTimecode += frameSize * 1000 / inputFrequency;
    return frameSize;
  }

  private void passDownstream(ByteBuffer buffer, int frameSize) throws InterruptedException {
    ByteBuffer nativeBuffer;

    if (!buffer.isDirect()) {
      if (directInput == null || directInput.capacity() < buffer.remaining()) {
        directInput = ByteBuffer.allocateDirect(buffer.remaining() + 200);
      }

      directInput.clear();
      directInput.put(buffer);
      directInput.flip();

      nativeBuffer = directInput;
    } else {
      nativeBuffer = buffer;
    }

    if (frameBuffer == null || frameBuffer.capacity() < frameSize * inputChannels) {
      frameBuffer = ByteBuffer.allocateDirect(frameSize * inputChannels * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
    }

    frameBuffer.clear();
    frameBuffer.limit(frameSize);

    opusDecoder.decode(nativeBuffer, frameBuffer);
    downstream.process(frameBuffer);
  }

  private void passThrough(ByteBuffer buffer) throws InterruptedException {
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);

    context.frameConsumer.consume(new AudioFrame(currentTimecode, bytes, 100, inputFormat));
  }

  private void checkDecoderNecessity() {
    if (FilterChainBuilder.isProcessingRequired(context, inputFormat)) {
      if (opusDecoder == null) {
        log.debug("Enabling reencode mode on opus track.");

        initialiseDecoder();

        AudioFrameVolumeChanger.apply(context);
      }
    } else {
      if (opusDecoder != null) {
        log.debug("Enabling passthrough mode on opus track.");

        destroyDecoder();

        AudioFrameVolumeChanger.apply(context);
      }
    }
  }

  private void initialiseDecoder() {
    opusDecoder = new OpusDecoder(inputFrequency, inputChannels);
    downstream = FilterChainBuilder.forShortPcm(context, inputChannels, inputFrequency, true);
    downstream.seekPerformed(currentTimecode, currentTimecode);
  }

  private void destroyDecoder() {
    opusDecoder.close();
    opusDecoder = null;
    downstream.close();
    downstream = null;
    directInput = null;
    frameBuffer = null;
  }
}
