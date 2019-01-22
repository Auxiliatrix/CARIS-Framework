package lavaplayer.container.wav;

import static lavaplayer.container.MediaContainerDetection.UNKNOWN_ARTIST;
import static lavaplayer.container.MediaContainerDetection.UNKNOWN_TITLE;
import static lavaplayer.container.MediaContainerDetection.checkNextBytes;
import static lavaplayer.container.wav.WavFileLoader.WAV_RIFF_HEADER;
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
 * Container detection probe for WAV format.
 */
public class WavContainerProbe implements MediaContainerProbe {
  private static final Logger log = LoggerFactory.getLogger(WavContainerProbe.class);

  @Override
  public String getName() {
    return "wav";
  }

  @Override
  public boolean matchesHints(MediaContainerHints hints) {
    return false;
  }

  @Override
  public MediaContainerDetectionResult probe(AudioReference reference, SeekableInputStream inputStream) throws IOException {
    if (!checkNextBytes(inputStream, WAV_RIFF_HEADER)) {
      return null;
    }

    log.debug("Track {} is a WAV file.", reference.identifier);

    WavFileInfo fileInfo = new WavFileLoader(inputStream).parseHeaders();

    return new MediaContainerDetectionResult(this, new AudioTrackInfo(
        defaultOnNull(reference.title, UNKNOWN_TITLE),
        UNKNOWN_ARTIST,
        fileInfo.getDuration(),
        reference.identifier,
        false,
        reference.identifier
    ));
  }

  @Override
  public AudioTrack createTrack(AudioTrackInfo trackInfo, SeekableInputStream inputStream) {
    return new WavAudioTrack(trackInfo, inputStream);
  }
}
