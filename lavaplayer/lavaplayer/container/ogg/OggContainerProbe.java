package lavaplayer.container.ogg;

import static lavaplayer.container.MediaContainerDetection.UNKNOWN_ARTIST;
import static lavaplayer.container.MediaContainerDetection.UNKNOWN_TITLE;
import static lavaplayer.container.MediaContainerDetection.checkNextBytes;
import static lavaplayer.container.ogg.OggPacketInputStream.OGG_PAGE_HEADER;

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
 * Container detection probe for OGG stream.
 */
public class OggContainerProbe implements MediaContainerProbe {
  private static final Logger log = LoggerFactory.getLogger(OggContainerProbe.class);

  @Override
  public String getName() {
    return "ogg";
  }

  @Override
  public boolean matchesHints(MediaContainerHints hints) {
    return false;
  }

  @Override
  public MediaContainerDetectionResult probe(AudioReference reference, SeekableInputStream stream) throws IOException {
    if (!checkNextBytes(stream, OGG_PAGE_HEADER)) {
      return null;
    }

    log.debug("Track {} is an OGG stream.", reference.identifier);

    return new MediaContainerDetectionResult(this, new AudioTrackInfo(
        reference.title != null ? reference.title : UNKNOWN_TITLE,
        UNKNOWN_ARTIST,
        Long.MAX_VALUE,
        reference.identifier,
        true,
        reference.identifier
    ));
  }

  @Override
  public AudioTrack createTrack(AudioTrackInfo trackInfo, SeekableInputStream inputStream) {
    return new OggAudioTrack(trackInfo, inputStream);
  }
}
