package lavaplayer.natives.vorbis;

import java.nio.ByteBuffer;

import lavaplayer.natives.ConnectorNativeLibLoader;

class VorbisDecoderLibrary {
  private VorbisDecoderLibrary() {

  }

  static VorbisDecoderLibrary getInstance() {
    ConnectorNativeLibLoader.loadConnectorLibrary();
    return new VorbisDecoderLibrary();
  }

  native long create();

  native void destroy(long instance);

  native int processHeader(long instance, ByteBuffer directBuffer, int offset, int length, boolean isBeginning);

  native boolean initialise(long instance);

  native int getChannelCount(long instance);

  native int input(long instance, ByteBuffer directBuffer, int offset, int length);

  native int output(long instance, float[][] channels, int length);
}
