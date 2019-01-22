package lavaplayer.source.stream;

import static lavaplayer.container.mpegts.MpegTsElementaryInputStream.ADTS_ELEMENTARY_STREAM;

import lavaplayer.container.adts.AdtsAudioTrack;
import lavaplayer.container.mpegts.MpegTsElementaryInputStream;
import lavaplayer.container.mpegts.PesPacketInputStream;
import lavaplayer.tools.io.ChainedInputStream;
import lavaplayer.tools.io.HttpInterface;
import lavaplayer.track.AudioTrackInfo;
import lavaplayer.track.DelegatedAudioTrack;
import lavaplayer.track.playback.LocalAudioTrackExecutor;

/**
 * Audio track that handles processing M3U segment streams which using MPEG-TS wrapped ADTS codec.
 */
public abstract class M3uStreamAudioTrack extends DelegatedAudioTrack {
  private final M3uStreamSegmentUrlProvider segmentUrlProvider;

  /**
   * @param trackInfo Track info
   */
  public M3uStreamAudioTrack(AudioTrackInfo trackInfo) {
    super(trackInfo);

    this.segmentUrlProvider = createSegmentProvider();
  }

  protected abstract M3uStreamSegmentUrlProvider createSegmentProvider();

  protected abstract HttpInterface getHttpInterface();

  @Override
  public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
    try (final HttpInterface httpInterface = getHttpInterface()) {
      try (ChainedInputStream chainedInputStream = new ChainedInputStream(() -> segmentUrlProvider.getNextSegmentStream(httpInterface))) {
        MpegTsElementaryInputStream elementaryInputStream = new MpegTsElementaryInputStream(chainedInputStream, ADTS_ELEMENTARY_STREAM);
        PesPacketInputStream pesPacketInputStream = new PesPacketInputStream(elementaryInputStream);

        processDelegate(new AdtsAudioTrack(trackInfo, pesPacketInputStream), localExecutor);
      }
    }
  }
}
