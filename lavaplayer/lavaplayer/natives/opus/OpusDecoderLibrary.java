package lavaplayer.natives.opus;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import lavaplayer.natives.ConnectorNativeLibLoader;

class OpusDecoderLibrary {
  private OpusDecoderLibrary() {

  }

  static OpusDecoderLibrary getInstance() {
    ConnectorNativeLibLoader.loadConnectorLibrary();
    return new OpusDecoderLibrary();
  }

  native long create(int sampleRate, int channels);

  native void destroy(long instance);

  native int decode(long instance, ByteBuffer directInput, int inputSize, ShortBuffer directOutput, int frameSize);
}
