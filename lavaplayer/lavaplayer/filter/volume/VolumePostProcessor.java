package lavaplayer.filter.volume;

import java.nio.ShortBuffer;

import lavaplayer.filter.AudioPostProcessor;
import lavaplayer.track.playback.AudioProcessingContext;

/**
 * Audio chunk post processor to apply selected volume.
 */
public class VolumePostProcessor implements AudioPostProcessor {
  private final PcmVolumeProcessor volumeProcessor;
  private final AudioProcessingContext context;

  /**
   * @param context Audio processing context to get volume from, and then one to use in case frame buffer needs to be
   *                rebuilt.
   */
  public VolumePostProcessor(AudioProcessingContext context) {
    this.context = context;
    this.volumeProcessor = new PcmVolumeProcessor(context.volumeLevel.get());
  }

  @Override
  public void process(long timecode, ShortBuffer buffer) throws InterruptedException {
    int currentVolume = context.volumeLevel.get();

    if (currentVolume != volumeProcessor.getLastVolume()) {
      AudioFrameVolumeChanger.apply(context);
    }

    // Volume 0 is stored in the frame with volume 100 buffer
    if (currentVolume != 0) {
      volumeProcessor.applyVolume(100, currentVolume, buffer);
    } else {
      volumeProcessor.setLastVolume(0);
    }
  }

  @Override
  public void close() {
    // Nothing to close here
  }
}
