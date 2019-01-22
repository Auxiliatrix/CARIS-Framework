package lavaplayer.player;

import lavaplayer.format.AudioDataFormat;
import lavaplayer.format.StandardAudioDataFormats;

/**
 * Configuration for audio processing.
 */
public class AudioConfiguration {
  public static final int OPUS_QUALITY_MAX = 10;

  private volatile ResamplingQuality resamplingQuality;
  private volatile int opusEncodingQuality;
  private volatile AudioDataFormat outputFormat;

  /**
   * Create a new configuration with default values.
   */
  public AudioConfiguration() {
    resamplingQuality = ResamplingQuality.LOW;
    opusEncodingQuality = OPUS_QUALITY_MAX;
    outputFormat = StandardAudioDataFormats.DISCORD_OPUS;
  }

  public ResamplingQuality getResamplingQuality() {
    return resamplingQuality;
  }

  public void setResamplingQuality(ResamplingQuality resamplingQuality) {
    this.resamplingQuality = resamplingQuality;
  }

  public int getOpusEncodingQuality() {
    return opusEncodingQuality;
  }

  public void setOpusEncodingQuality(int opusEncodingQuality) {
    this.opusEncodingQuality = Math.max(0, Math.min(opusEncodingQuality, OPUS_QUALITY_MAX));
  }

  public AudioDataFormat getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(AudioDataFormat outputFormat) {
    this.outputFormat = outputFormat;
  }

  /**
   * @return A copy of this configuration.
   */
  public AudioConfiguration copy() {
    AudioConfiguration copy = new AudioConfiguration();
    copy.setResamplingQuality(resamplingQuality);
    copy.setOpusEncodingQuality(opusEncodingQuality);
    copy.setOutputFormat(outputFormat);
    return copy;
  }

  /**
   * Resampling quality levels
   */
  public enum ResamplingQuality {
    HIGH,
    MEDIUM,
    LOW
  }
}
