package lavaplayer.source.youtube;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import lavaplayer.tools.io.HttpInterface;
import lavaplayer.tools.io.PersistentHttpStream;

/**
 * A persistent HTTP stream implementation that uses the range parameter instead of HTTP headers for specifying
 * the start position at which to start reading on a new connection.
 */
public class YoutubePersistentHttpStream extends PersistentHttpStream {

  /**
   * @param httpInterface The HTTP interface to use for requests
   * @param contentUrl The URL of the resource
   * @param contentLength The length of the resource in bytes
   */
  public YoutubePersistentHttpStream(HttpInterface httpInterface, URI contentUrl, long contentLength) {
    super(httpInterface, contentUrl, contentLength);
  }

  @Override
  protected URI getConnectUrl() {
    if (position > 0) {
      try {
        return new URIBuilder(contentUrl).addParameter("range", position + "-" + contentLength).build();
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    } else {
      return contentUrl;
    }
  }

  @Override
  protected boolean useHeadersForRange() {
    return false;
  }

  @Override
  protected boolean canSeekHard() {
    return true;
  }
}
