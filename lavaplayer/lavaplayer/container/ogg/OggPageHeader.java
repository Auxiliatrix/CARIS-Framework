package lavaplayer.container.ogg;

/**
 * Header of an OGG stream page.
 */
public class OggPageHeader {
  private static final int FLAG_CONTINUATION = 0x01;
  private static final int FLAG_FIRST_PAGE = 0x02;
  private static final int FLAG_LAST_PAGE = 0x04;

  /**
   * If this page starts in the middle of a packet that was left incomplete in the previous page.
   */
  public final boolean isContinuation;
  /**
   * If this is the first page of the track.
   */
  public final boolean isFirstPage;
  /**
   * If this is the last page of the track.
   */
  public final boolean isLastPage;
  /**
   * The absolute position in the number of samples of this packet relative to the track start.
   */
  public final long absolutePosition;
  /**
   * Unique identifier of this track in the stream.
   */
  public final int streamIdentifier;
  /**
   * The index of the page within a track.
   */
  public final int pageSequence;
  /**
   * The checksum of the page.
   */
  public final int pageChecksum;
  /**
   * Number of segments in the page.
   */
  public final int segmentCount;

  /**
   * @param flags Page flags.
   * @param absolutePosition The absolute position in the number of samples of this packet relative to the track start.
   * @param streamIdentifier Unique identifier of this track in the stream.
   * @param pageSequence The index of the page within a track.
   * @param checksum The checksum of the page.
   * @param segmentCount Number of segments in the page.
   */
  public OggPageHeader(int flags, long absolutePosition, int streamIdentifier, int pageSequence, int checksum, int segmentCount) {
    this.isContinuation = (flags & FLAG_CONTINUATION) != 0;
    this.isFirstPage = (flags & FLAG_FIRST_PAGE) != 0;
    this.isLastPage = (flags & FLAG_LAST_PAGE) != 0;
    this.absolutePosition = absolutePosition;
    this.streamIdentifier = streamIdentifier;
    this.pageSequence = pageSequence;
    this.pageChecksum = checksum;
    this.segmentCount = segmentCount;
  }
}
