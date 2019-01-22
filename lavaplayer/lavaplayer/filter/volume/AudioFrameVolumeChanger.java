package lavaplayer.filter.volume;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import lavaplayer.format.AudioDataFormat;
import lavaplayer.format.transcoder.AudioChunkDecoder;
import lavaplayer.format.transcoder.AudioChunkEncoder;
import lavaplayer.format.transcoder.OpusChunkDecoder;
import lavaplayer.format.transcoder.OpusChunkEncoder;
import lavaplayer.format.transcoder.PcmChunkDecoder;
import lavaplayer.format.transcoder.PcmChunkEncoder;
import lavaplayer.player.AudioConfiguration;
import lavaplayer.track.playback.AudioFrame;
import lavaplayer.track.playback.AudioFrameRebuilder;
import lavaplayer.track.playback.AudioProcessingContext;

/**
 * A frame rebuilder to apply a specific volume level to the frames.
 */
public class AudioFrameVolumeChanger implements AudioFrameRebuilder {
  private final AudioConfiguration configuration;
  private final AudioDataFormat format;
  private final int newVolume;
  private final ShortBuffer sampleBuffer;
  private final PcmVolumeProcessor volumeProcessor;

  private AudioChunkEncoder encoder;
  private AudioChunkDecoder decoder;
  private int frameIndex;

  private AudioFrameVolumeChanger(AudioConfiguration configuration, AudioDataFormat format, int newVolume) {
    this.configuration = configuration;
    this.format = format;
    this.newVolume = newVolume;

    this.sampleBuffer = ByteBuffer.allocateDirect(format.bufferSize(2)).order(ByteOrder.nativeOrder()).asShortBuffer();
    this.volumeProcessor = new PcmVolumeProcessor(100);
  }

  @Override
  public AudioFrame rebuild(AudioFrame frame) {
    if (frame.volume == newVolume) {
      return frame;
    }

    decoder.decode(frame.data, sampleBuffer);

    int targetVolume = newVolume;

    if (++frameIndex < 50) {
      targetVolume = (int) ((newVolume - frame.volume) * (frameIndex / 50.0) + frame.volume);
    }

    // Volume 0 is stored in the frame with volume 100 buffer
    if (targetVolume != 0) {
      volumeProcessor.applyVolume(frame.volume, targetVolume, sampleBuffer);
    }

    byte[] bytes = encoder.encode(sampleBuffer);

    // One frame per 20ms is consumed. To not spike the CPU usage, reencode only once per 5ms. By the time the buffer is
    // fully rebuilt, it is probably near to 3/4 its maximum size.
    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {
      // Keep it interrupted, it will trip on the next interruptible operation
      Thread.currentThread().interrupt();
    }

    return new AudioFrame(frame.timecode, bytes, targetVolume, format);
  }

  private void setupLibraries() {
    if (format.codec == AudioDataFormat.Codec.OPUS) {
      encoder = new OpusChunkEncoder(configuration, format);
      decoder = new OpusChunkDecoder(format);
    } else {
      encoder = new PcmChunkEncoder(format);
      decoder = new PcmChunkDecoder(format);
    }
  }

  private void clearLibraries() {
    if (encoder != null) {
      encoder.close();
    }

    if (decoder != null) {
      decoder.close();
    }
  }

  /**
   * Applies a volume level to the buffered frames of a frame consumer
   * @param context Audio processing context which contains the format information as well as the frame buffer
   */
  public static void apply(AudioProcessingContext context) {
    AudioFrameVolumeChanger volumeChanger = new AudioFrameVolumeChanger(context.configuration, context.outputFormat, context.volumeLevel.get());

    try {
      volumeChanger.setupLibraries();
      context.frameConsumer.rebuild(volumeChanger);
    } finally {
      volumeChanger.clearLibraries();
    }
  }
}
