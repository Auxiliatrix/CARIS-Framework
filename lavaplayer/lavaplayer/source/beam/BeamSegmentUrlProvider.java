package lavaplayer.source.beam;

import static lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;
import static lavaplayer.tools.io.HttpClientTools.fetchResponseLines;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.source.stream.ExtendedM3uParser;
import lavaplayer.source.stream.M3uStreamSegmentUrlProvider;
import lavaplayer.tools.FriendlyException;
import lavaplayer.tools.io.HttpInterface;

/**
 * Provider for Beam segment URLs from a channel.
 */
public class BeamSegmentUrlProvider extends M3uStreamSegmentUrlProvider {
  private static final Logger log = LoggerFactory.getLogger(BeamSegmentUrlProvider.class);

  private final String channelId;
  private String streamSegmentPlaylistUrl;
  private String lastSegment;

  /**
   * @param channelId Channel ID number.
   */
  public BeamSegmentUrlProvider(String channelId) {
    this.channelId = channelId;
  }

  @Override
  protected String getQualityFromM3uDirective(ExtendedM3uParser.Line directiveLine) {
    return directiveLine.directiveArguments.get("NAME");
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
      String segment = chooseNextSegment(segments, lastSegment);

      if (segment == null) {
        return null;
      }

      lastSegment = segment;
      return createSegmentUrl(streamSegmentPlaylistUrl, segment);
    } catch (IOException e) {
      throw new FriendlyException("Failed to get next part of the stream.", SUSPICIOUS, e);
    }
  }

  private boolean obtainSegmentPlaylistUrl(HttpInterface httpInterface) throws IOException {
    if (streamSegmentPlaylistUrl != null) {
      return true;
    }

    HttpUriRequest request = new HttpGet("https://beam.pro/api/v1/channels/" + channelId + "/manifest.m3u8");
    List<ChannelStreamInfo> streams = loadChannelStreamsList(fetchResponseLines(httpInterface, request, "beam channel streams list"));

    if (streams.isEmpty()) {
      throw new IllegalStateException("No streams available on channel.");
    }

    ChannelStreamInfo stream = streams.get(0);

    log.debug("Chose stream with quality {} from url {}", stream.quality, stream.url);
    streamSegmentPlaylistUrl = stream.url;
    return true;
  }
}
