package lavaplayer.container.mpeg;

import static lavaplayer.container.MediaContainerDetection.UNKNOWN_ARTIST;
import static lavaplayer.container.MediaContainerDetection.UNKNOWN_TITLE;
import static lavaplayer.container.MediaContainerDetection.checkNextBytes;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.container.MediaContainerDetectionResult;
import lavaplayer.container.MediaContainerHints;
import lavaplayer.container.MediaContainerProbe;
import lavaplayer.container.mpeg.reader.MpegFileTrackProvider;
import lavaplayer.tools.DataFormatTools;
import lavaplayer.tools.io.SeekableInputStream;
import lavaplayer.track.AudioReference;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;

/**
 * Container detection probe for MP4 format.
 */
public class MpegContainerProbe implements MediaContainerProbe {
  private static final Logger log = LoggerFactory.getLogger(MpegContainerProbe.class);

  private static final int[] ISO_TAG = new int[] { 0x00, 0x00, 0x00, -1, 0x66, 0x74, 0x79, 0x70 };

  @Override
  public String getName() {
    return "mp4";
  }

  @Override
  public boolean matchesHints(MediaContainerHints hints) {
    return false;
  }

  @Override
  public MediaContainerDetectionResult probe(AudioReference reference, SeekableInputStream inputStream) throws IOException {
    if (!checkNextBytes(inputStream, ISO_TAG)) {
      return null;
    }

    log.debug("Track {} is an MP4 file.", reference.identifier);

    MpegFileLoader file = new MpegFileLoader(inputStream);
    file.parseHeaders();

    MpegTrackInfo audioTrack = getSupportedAudioTrack(file);

    if (audioTrack == null) {
      return new MediaContainerDetectionResult(this, "No supported audio format in the MP4 file.");
    }

    MpegTrackConsumer trackConsumer = new MpegNoopTrackConsumer(audioTrack);
    MpegFileTrackProvider fileReader = file.loadReader(trackConsumer);

    if (fileReader == null) {
      return new MediaContainerDetectionResult(this, "MP4 file uses an unsupported format.");
    }

    return new MediaContainerDetectionResult(this, new AudioTrackInfo(
        DataFormatTools.defaultOnNull(file.getTextMetadata("Title"), UNKNOWN_TITLE),
        DataFormatTools.defaultOnNull(file.getTextMetadata("Artist"), UNKNOWN_ARTIST),
        fileReader.getDuration(),
        reference.identifier,
        false,
        reference.identifier
    ));
  }

  @Override
  public AudioTrack createTrack(AudioTrackInfo trackInfo, SeekableInputStream inputStream) {
    return new MpegAudioTrack(trackInfo, inputStream);
  }

  private MpegTrackInfo getSupportedAudioTrack(MpegFileLoader file) {
    for (MpegTrackInfo track : file.getTrackList()) {
      if ("soun".equals(track.handler) && "mp4a".equals(track.codecName)) {
        return track;
      }
    }

    return null;
  }
}
