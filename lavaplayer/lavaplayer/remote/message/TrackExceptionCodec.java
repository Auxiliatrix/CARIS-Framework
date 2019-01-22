package lavaplayer.remote.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import lavaplayer.tools.ExceptionTools;

/**
 * Codec for track exception message.
 */
public class TrackExceptionCodec implements RemoteMessageCodec<TrackExceptionMessage> {
  @Override
  public Class<TrackExceptionMessage> getMessageClass() {
    return TrackExceptionMessage.class;
  }

  @Override
  public int version(RemoteMessage message) {
    return 1;
  }

  @Override
  public void encode(DataOutput out, TrackExceptionMessage message) throws IOException {
    out.writeLong(message.executorId);
    ExceptionTools.encodeException(out, message.exception);
  }

  @Override
  public TrackExceptionMessage decode(DataInput in, int version) throws IOException {
    return new TrackExceptionMessage(in.readLong(), ExceptionTools.decodeException(in));
  }
}
