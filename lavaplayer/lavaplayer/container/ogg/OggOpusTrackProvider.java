package lavaplayer.container.ogg;

import java.io.IOException;
import java.nio.ByteBuffer;

import lavaplayer.container.common.OpusPacketRouter;
import lavaplayer.tools.io.DirectBufferStreamBroker;
import lavaplayer.track.playback.AudioProcessingContext;

/**
 * OGG stream handler for Opus codec.
 */
public class OggOpusTrackProvider implements OggTrackProvider {
  private static final int HEAD_TAG_HALF = ByteBuffer.wrap(new byte[] { 'H', 'e', 'a', 'd' }).getInt();

  private final OggPacketInputStream packetInputStream;
  private final DirectBufferStreamBroker broker;
  private OpusPacketRouter opusPacketRouter;

  /**
   * @param packetInputStream OGG packet input stream
   * @param broker Broker for loading stream data into direct byte buffer, it has already loaded the first packet of the
   *               stream at this point.
   */
  public OggOpusTrackProvider(OggPacketInputStream packetInputStream, DirectBufferStreamBroker broker) {
    this.packetInputStream = packetInputStream;
    this.broker = broker;
  }

  @Override
  public void initialise(AudioProcessingContext context) throws IOException {
    ByteBuffer buffer = broker.getBuffer();

    if (buffer.getInt(4) != HEAD_TAG_HALF) {
      throw new IllegalStateException("First packet is not an OpusHead.");
    }

    int channelCount = buffer.get(9) & 0xFF;
    int sampleRate = Integer.reverseBytes(buffer.getInt(12));

    opusPacketRouter = new OpusPacketRouter(context, sampleRate, channelCount);

    if (!packetInputStream.startNewPacket()) {
      throw new IllegalStateException("No OpusTags packet in track.");
    }

    broker.consume(true, packetInputStream);
  }

  @Override
  public void provideFrames() throws InterruptedException {
    try {
      while (packetInputStream.startNewPacket()) {
        broker.consume(true, packetInputStream);

        ByteBuffer buffer = broker.getBuffer();

        if (buffer.remaining() > 0) {
          opusPacketRouter.process(buffer);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void seekToTimecode(long timecode) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
    if (opusPacketRouter != null) {
      opusPacketRouter.close();
    }
  }
}
