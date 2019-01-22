package lavaplayer.container.flac;

import static lavaplayer.container.MediaContainerDetection.UNKNOWN_ARTIST;
import static lavaplayer.container.MediaContainerDetection.UNKNOWN_TITLE;
import static lavaplayer.container.MediaContainerDetection.checkNextBytes;
import static lavaplayer.tools.DataFormatTools.defaultOnNull;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.container.MediaContainerDetectionResult;
import lavaplayer.container.MediaContainerHints;
import lavaplayer.container.MediaContainerProbe;
import lavaplayer.tools.io.SeekableInputStream;
import lavaplayer.track.AudioReference;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;

/**
 * Container detection probe for MP3 format.
 */
public class FlacContainerProbe implements MediaContainerProbe {
  private static final Logger log = LoggerFactory.getLogger(FlacContainerProbe.class);

  private static final String TITLE_TAG = "TITLE";
  private static final String ARTIST_TAG = "ARTIST";

  @Override
  public String getName() {
    return "flac";
  }

  @Override
  public boolean matchesHints(MediaContainerHints hints) {
    return false;
  }

  @Override
  public MediaContainerDetectionResult probe(AudioReference reference, SeekableInputStream inputStream) throws IOException {
    if (!checkNextBytes(inputStream, FlacFileLoader.FLAC_CC)) {
      return null;
    }

    log.debug("Track {} is a FLAC file.", reference.identifier);

    FlacTrackInfo trackInfo = new FlacFileLoader(inputStream).parseHeaders();

    return new MediaContainerDetectionResult(this, new AudioTrackInfo(
        defaultOnNull(trackInfo.tags.get(TITLE_TAG), UNKNOWN_TITLE),
        defaultOnNull(trackInfo.tags.get(ARTIST_TAG), UNKNOWN_ARTIST),
        trackInfo.duration,
        reference.identifier,
        false,
        reference.identifier
    ));
  }

  @Override
  public AudioTrack createTrack(AudioTrackInfo trackInfo, SeekableInputStream inputStream) {
    return new FlacAudioTrack(trackInfo, inputStream);
  }
}
