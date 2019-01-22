package lavaplayer.source.twitch;

import static lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

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
 * Audio source manager which detects Twitch tracks by URL.
 */
public class TwitchStreamAudioSourceManager implements AudioSourceManager, HttpConfigurable {
  private static final String STREAM_NAME_REGEX = "^https://(?:www\\.|go\\.)?twitch.tv/([^/]+)$";
  private static final Pattern streamNameRegex = Pattern.compile(STREAM_NAME_REGEX);

  public static final String CLIENT_ID = "jzkbprff40iqj646a697cyrvl0zt2m6";

  private final HttpInterfaceManager httpInterfaceManager;

  /**
   * Create an instance.
   */
  public TwitchStreamAudioSourceManager() {
    httpInterfaceManager = HttpClientTools.createDefaultThreadLocalManager();
  }

  @Override
  public String getSourceName() {
    return "twitch";
  }

  @Override
  public AudioItem loadItem(DefaultAudioPlayerManager manager, AudioReference reference) {
    String streamName = getChannelIdentifierFromUrl(reference.identifier);
    if (streamName == null) {
      return null;
    }

    JsonBrowser channelInfo = fetchStreamChannelInfo(streamName);

    if (channelInfo == null) {
      return AudioReference.NO_TRACK;
    } else {
      final String displayName = channelInfo.get("display_name").text();
      final String status = channelInfo.get("status").text();

      return new TwitchStreamAudioTrack(new AudioTrackInfo(
          status,
          displayName,
          Long.MAX_VALUE,
          reference.identifier,
          true,
          reference.identifier
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
    return new TwitchStreamAudioTrack(trackInfo, this);
  }

  /**
   * Extract channel identifier from a channel URL.
   * @param url Channel URL
   * @return Channel identifier (for API requests)
   */
  public static String getChannelIdentifierFromUrl(String url) {
    Matcher matcher = streamNameRegex.matcher(url);
    if (!matcher.matches()) {
      return null;
    }

    return matcher.group(1);
  }

  /**
   * @param url Request URL
   * @return Request with necessary headers attached.
   */
  public static HttpUriRequest createGetRequest(String url) {
    return addClientHeaders(new HttpGet(url));
  }

  /**
   * @param url Request URL
   * @return Request with necessary headers attached.
   */
  public static HttpUriRequest createGetRequest(URI url) {
    return addClientHeaders(new HttpGet(url));
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

  private static HttpUriRequest addClientHeaders(HttpUriRequest request) {
    request.setHeader("Client-ID", CLIENT_ID);
    return request;
  }

  private JsonBrowser fetchStreamChannelInfo(String name) {
    try (HttpInterface httpInterface = getHttpInterface()) {
      HttpUriRequest request = createGetRequest("https://api.twitch.tv/api/channels/" + name + "/ember?on_site=1");

      return HttpClientTools.fetchResponseAsJson(httpInterface, request);
    } catch (IOException e) {
      throw new FriendlyException("Loading Twitch channel information failed.", SUSPICIOUS, e);
    }
  }

  @Override
  public void shutdown() {
    // Nothing to shut down
  }
}
