package lavaplayer.source.beam;

import static lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.source.AudioSourceManager;
import lavaplayer.tools.FriendlyException;
import lavaplayer.tools.JsonBrowser;
import lavaplayer.tools.io.HttpClientTools;
import lavaplayer.tools.io.HttpConfigurable;
import lavaplayer.tools.io.HttpInterface;
import lavaplayer.tools.io.HttpInterfaceManager;
import lavaplayer.track.AudioItem;
import lavaplayer.track.AudioReference;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackInfo;

/**
 * Audio source manager which detects Beam.pro tracks by URL.
 */
public class BeamAudioSourceManager implements AudioSourceManager, HttpConfigurable {
  private static final String STREAM_NAME_REGEX = "^https://(?:www\\.)?(?:beam\\.pro|mixer\\.com)/([^/]+)$";
  private static final Pattern streamNameRegex = Pattern.compile(STREAM_NAME_REGEX);

  private final HttpInterfaceManager httpInterfaceManager;

  /**
   * Create an instance.
   */
  public BeamAudioSourceManager() {
    this.httpInterfaceManager = HttpClientTools.createDefaultThreadLocalManager();
  }

  @Override
  public String getSourceName() {
    return "beam.pro";
  }

  @Override
  public AudioItem loadItem(DefaultAudioPlayerManager manager, AudioReference reference) {
    String streamName = getChannelNameFromUrl(reference.identifier);
    if (streamName == null) {
      return null;
    }

    JsonBrowser channelInfo = fetchStreamChannelInfo(streamName);

    if (channelInfo == null) {
      return AudioReference.NO_TRACK;
    } else {
      String displayName = channelInfo.get("name").text();
      String id = channelInfo.get("id").text();

      if (displayName == null || id == null) {
        throw new IllegalStateException("Expected id and name fields from Beam channel info.");
      }

      return new BeamAudioTrack(new AudioTrackInfo(
          displayName,
          streamName,
          Long.MAX_VALUE,
          id + "|" + streamName + "|" + reference.identifier,
          true,
          "https://beam.pro/" + streamName
      ), this);
    }
  }

  @Override
  public boolean isTrackEncodable(AudioTrack track) {
    return true;
  }

  @Override
  public void encodeTrack(AudioTrack track, DataOutput output) throws IOException {
    // Nothing special to do, URL (identifier) is enough
  }

  @Override
  public AudioTrack decodeTrack(AudioTrackInfo trackInfo, DataInput input) throws IOException {
    return new BeamAudioTrack(trackInfo, this);
  }

  @Override
  public void shutdown() {
    // Nothing to shut down.
  }

  private static String getChannelNameFromUrl(String url) {
    Matcher matcher = streamNameRegex.matcher(url);
    if (!matcher.matches()) {
      return null;
    }

    return matcher.group(1);
  }

  private JsonBrowser fetchStreamChannelInfo(String name) {
    try (HttpInterface httpInterface = getHttpInterface()) {
      return HttpClientTools.fetchResponseAsJson(httpInterface, new HttpGet("https://beam.pro/api/v1/channels/" + name));
    } catch (IOException e) {
      throw new FriendlyException("Loading Beam channel information failed.", SUSPICIOUS, e);
    }
  }

  /**
   * @return Get an HTTP interface for a playing track.
   */
  public HttpInterface getHttpInterface() {
    return httpInterfaceManager.getInterface();
  }

  @Override
  public void configureRequests(Function<RequestConfig, RequestConfig> configurator) {
    httpInterfaceManager.configureRequests(configurator);
  }
}
