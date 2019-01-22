package lavaplayer.source.youtube;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lavaplayer.tools.DataFormatTools;
import lavaplayer.tools.ExceptionTools;
import lavaplayer.tools.io.HttpInterface;
import lavaplayer.track.AudioItem;
import lavaplayer.track.AudioReference;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.BasicAudioPlaylist;

/**
 * Handles processing YouTube searches.
 */
public class YoutubeSearchProvider {
  private static final Logger log = LoggerFactory.getLogger(YoutubeSearchProvider.class);

  private final YoutubeAudioSourceManager sourceManager;

  /**
   * @param sourceManager YouTube source manager used for created tracks.
   */
  public YoutubeSearchProvider(YoutubeAudioSourceManager sourceManager) {
    this.sourceManager = sourceManager;
  }

  /**
   * @param query Search query.
   * @return Playlist of the first page of results.
   */
  public AudioItem loadSearchResult(String query) {
    log.debug("Performing a search with query {}", query);

    try (HttpInterface httpInterface = sourceManager.getHttpInterface()) {
      URI url = new URIBuilder("https://www.youtube.com/results").addParameter("search_query", query).build();

      try (CloseableHttpResponse response = httpInterface.execute(new HttpGet(url))) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
          throw new IOException("Invalid status code for search response: " + statusCode);
        }

        Document document = Jsoup.parse(response.getEntity().getContent(), StandardCharsets.UTF_8.name(), "");
        return extractSearchResults(document, query);
      }
    } catch (Exception e) {
      throw ExceptionTools.wrapUnfriendlyExceptions(e);
    }
  }

  private AudioItem extractSearchResults(Document document, String query) {
    List<AudioTrack> tracks = new ArrayList<>();

    for (Element results : document.select("#page > #content #results")) {
      for (Element result : results.select(".yt-lockup-video")) {
        if (!result.hasAttr("data-ad-impressions") && result.select(".standalone-ypc-badge-renderer-label").isEmpty()) {
          extractTrackFromResultEntry(tracks, result);
        }
      }
    }

    if (tracks.isEmpty()) {
      return AudioReference.NO_TRACK;
    } else {
      return new BasicAudioPlaylist("Search results for: " + query, tracks, null, true);
    }
  }

  private void extractTrackFromResultEntry(List<AudioTrack> tracks, Element element) {
    Element durationElement = element.select("[class^=video-time]").first();
    Element contentElement = element.select(".yt-lockup-content").first();
    String videoId = element.attr("data-context-item-id");

    if (durationElement == null || contentElement == null || videoId.isEmpty()) {
      return;
    }

    long duration = DataFormatTools.durationTextToMillis(durationElement.text());

    String title = contentElement.select(".yt-lockup-title > a").text();
    String author = contentElement.select(".yt-lockup-byline > a").text();

    tracks.add(sourceManager.buildTrackObject(videoId, title, author, false, duration));
  }
}
