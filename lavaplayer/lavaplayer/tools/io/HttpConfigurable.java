package lavaplayer.tools.io;

import java.util.function.Function;

import org.apache.http.client.config.RequestConfig;

/**
 * Represents a class where HTTP request configuration can be changed.
 */
public interface HttpConfigurable {
  /**
   * @param configurator Function to reconfigure request config.
   */
  void configureRequests(Function<RequestConfig, RequestConfig> configurator);
}
