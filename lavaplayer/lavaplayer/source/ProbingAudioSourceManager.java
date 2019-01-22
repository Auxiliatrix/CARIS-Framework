package lavaplayer.source;

import static lavaplayer.tools.FriendlyException.Severity.COMMON;

import lavaplayer.container.MediaContainerDetectionResult;
import lavaplayer.container.MediaContainerProbe;
import lavaplayer.tools.FriendlyException;
import lavaplayer.track.AudioItem;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;

/**
 * The base class for audio sources which use probing to detect container type.
 */
public abstract class ProbingAudioSourceManager implements AudioSourceManager {
  protected AudioItem handleLoadResult(MediaContainerDetectionResult result) {
    if (result != null) {
      if (result.isReference()) {
        return result.getReference();
      } else if (!result.isContainerDetected()) {
        throw new FriendlyException("Unknown file format.", COMMON, null);
      } else if (!result.isSupportedFile()) {
        throw new FriendlyException(result.getUnsupportedReason(), COMMON, null);
      } else {
        return createTrack(result.getTrackInfo(), result.getContainerProbe());
      }
    }

    return null;
  }

  protected abstract AudioTrack createTrack(AudioTrackInfo trackInfo, MediaContainerProbe probe);
}
