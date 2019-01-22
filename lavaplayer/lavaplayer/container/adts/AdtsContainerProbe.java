package lavaplayer.container.adts;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.container.MediaContainerDetection;
import lavaplayer.container.MediaContainerDetectionResult;
import lavaplayer.container.MediaContainerHints;
import lavaplayer.container.MediaContainerProbe;
import lavaplayer.tools.io.SeekableInputStream;
import lavaplayer.track.AudioReference;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;

/**
 * Container detection probe for ADTS stream format.
 */
public class AdtsContainerProbe implements MediaContainerProbe {
  private static final Logger log = LoggerFactory.getLogger(AdtsContainerProbe.class);

  @Override
  public String getName() {
    return "adts";
  }

  @Override
  public boolean matchesHints(MediaContainerHints hints) {
    return false;
  }

  @Override
  public MediaContainerDetectionResult probe(AudioReference reference, SeekableInputStream inputStream) throws IOException {
    AdtsStreamReader reader = new AdtsStreamReader(inputStream);

    if (reader.findPacketHeader(MediaContainerDetection.STREAM_SCAN_DISTANCE) == null) {
      return null;
    }

    log.debug("Track {} is an ADTS stream.", reference.identifier);

    return new MediaContainerDetectionResult(this, new AudioTrackInfo(
        reference.title != null ? reference.title : MediaContainerDetection.UNKNOWN_TITLE,
        MediaContainerDetection.UNKNOWN_ARTIST,
        Long.MAX_VALUE,
        reference.identifier,
        true,
        reference.identifier
    ));
  }

  @Override
  public AudioTrack createTrack(AudioTrackInfo trackInfo, SeekableInputStream inputStream) {
    return new AdtsAudioTrack(trackInfo, inputStream);
  }
}
