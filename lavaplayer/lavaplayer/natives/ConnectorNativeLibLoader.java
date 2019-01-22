package lavaplayer.natives;

import static lavaplayer.natives.NativeLibLoader.WIN_X86;
import static lavaplayer.natives.NativeLibLoader.WIN_X86_64;

/**
 * Methods for loading the connector library.
 */
public class ConnectorNativeLibLoader {
  /**
   * Loads the connector library with its dependencies for the current system
   */
  public static void loadConnectorLibrary() {
    NativeLibLoader.load(ConnectorNativeLibLoader.class, "libmpg123-0", WIN_X86_64);
    NativeLibLoader.load(ConnectorNativeLibLoader.class, "libgcc_s_sjlj-1", WIN_X86);
    NativeLibLoader.load(ConnectorNativeLibLoader.class, "libmpg123-0", WIN_X86);
    NativeLibLoader.load(ConnectorNativeLibLoader.class, "connector");
  }
}
