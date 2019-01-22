package lavaplayer.source.twitch;

import static lavaplayer.source.twitch.TwitchStreamAudioSourceManager.createGetRequest;
import static lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.source.stream.ExtendedM3uParser;
import lavaplayer.source.stream.M3uStreamSegmentUrlProvider;
import lavaplayer.tools.FriendlyException;
import lavaplayer.tools.JsonBrowser;
import lavaplayer.tools.io.HttpClientTools;
import lavaplayer.tools.io.HttpInterface;

/**
 * Provider for Twitch segment URLs from a channel.
 */
public class TwitchStreamSegmentUrlProvider extends M3uStreamSegmentUrlProvider {
  private static final String TOKEN_PARAMETER = "token";

  private static final Logger log = LoggerFactory.getLogger(TwitchStreamSegmentUrlProvider.class);

  private final String channelName;
  private String streamSegmentPlaylistUrl;
  private String lastSegment;
  private long tokenExpirationTime;

  /**
   * @param channelName Channel identifier.
   */
  public TwitchStreamSegmentUrlProvider(String channelName) {
    this.channelName = channelName;
    this.tokenExpirationTime = -1;
  }

  @Override
  protected String getQualityFromM3uDirective(ExtendedM3uParser.Line directiveLine) {
    return directiveLine.directiveArguments.get("VIDEO");
  }

  /**
   * @param httpInterface Http interface to use for requests.
   * @return The URL of the next TS segment.
   */
  @Override
  public String getNextSegmentUrl(HttpInterface httpInterface) {
    try {
      if (!obtainSegmentPlaylistUrl(httpInterface)) {
        return null;
      }

      List<String> segments = loadStreamSegmentsList(httpInterface, streamSegmentPlaylistUrl);
      lastSegment = chooseNextSegment(segments, lastSegment);

      if (lastSegment == null) {
        return null;
      }

      return createSegmentUrl(streamSegmentPlaylistUrl, lastSegment);
    } catch (IOException e) {
      throw new FriendlyException("Failed to get next part of the stream.", SUSPICIOUS, e);
    }
  }

  private boolean obtainSegmentPlaylistUrl(HttpInterface httpInterface) throws IOException {
    if (System.currentTimeMillis() < tokenExpirationTime) {
      return true;
    }

    JsonBrowser token = loadAccessToken(httpInterface);
    HttpUriRequest request = new HttpGet(getChannelStreamsUrl(token).toString());
    ChannelStreams streams = loadChannelStreamsInfo(HttpClientTools.fetchResponseLines(httpInterface, request, "channel streams list"));

    if (streams.entries.isEmpty()) {
      throw new IllegalStateException("No streams available on channel.");
    }

    ChannelStreamInfo stream = streams.entries.get(0);

    log.debug("Chose stream with quality {} from url {}", stream.quality, stream.url);
    streamSegmentPlaylistUrl = stream.url;

    long tokenServerExpirationTime = JsonBrowser.parse(token.get(TOKEN_PARAMETER).text()).get("expires").as(Long.class) * 1000L;
    tokenExpirationTime = System.currentTimeMillis() + (tokenServerExpirationTime - streams.serverTime) - 5000;

    return true;
  }

  private JsonBrowser loadAccessToken(HttpInterface httpInterface) throws IOException {
    HttpUriRequest request = createGetRequest("https://api.twitch.tv/api/channels/" + channelName +
        "/access_token?adblock=false&need_https=true&platform=web&player_type=site");

    try (CloseableHttpResponse response = httpInterface.execute(request)) {
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != 200) {
        throw new IOException("Unexpected response code from access token request: " + statusCode);
      }

      return JsonBrowser.parse(response.getEntity().getContent());
    }
  }

  private ChannelStreams loadChannelStreamsInfo(String[] lines) throws IOException {
    List<ChannelStreamInfo> streams = loadChannelStreamsList(lines);
    ExtendedM3uParser.Line twitchInfoLine = null;

    for (String lineText : lines) {
      ExtendedM3uParser.Line line = ExtendedM3uParser.parseLine(lineText);

      if (line.isDirective() && "EXT-X-TWITCH-INFO".equals(line.directiveName)) {
        twitchInfoLine = line;
      }
    }

    return buildChannelStreamsInfo(twitchInfoLine, streams);
  }

  private ChannelStreams buildChannelStreamsInfo(ExtendedM3uParser.Line twitchInfoLine, List<ChannelStreamInfo> streams) {
    String serverTimeValue = twitchInfoLine != null ? twitchInfoLine.directiveArguments.get("SERVER-TIME") : null;

    if (serverTimeValue == null) {
      throw new IllegalStateException("Required server time information not available.");
    }

    return new ChannelStreams(
        (long) (Double.valueOf(serverTimeValue) * 1000.0),
        streams
    );
  }

  private URI getChannelStreamsUrl(JsonBrowser token) {
    try {
      return new URIBuilder("https://usher.ttvnw.net/api/channel/hls/" + channelName + ".m3u8")
          .addParameter("token", token.get(TOKEN_PARAMETER).text())
          .addParameter("sig", token.get("sig").text())
          .addParameter("allow_source", "true")
          .addParameter("allow_spectre", "true")
          .addParameter("player_backend", "html5")
          .addParameter("expgroup", "regular")
          .build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static class ChannelStreams {
    private final long serverTime;
    private final List<ChannelStreamInfo> entries;

    private ChannelStreams(long serverTime, List<ChannelStreamInfo> entries) {
      this.serverTime = serverTime;
      this.entries = entries;
    }
  }
}
