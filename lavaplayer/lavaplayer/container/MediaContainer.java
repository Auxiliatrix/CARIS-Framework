package lavaplayer.container;

import lavaplayer.container.adts.AdtsContainerProbe;
import lavaplayer.container.flac.FlacContainerProbe;
import lavaplayer.container.matroska.MatroskaContainerProbe;
import lavaplayer.container.mp3.Mp3ContainerProbe;
import lavaplayer.container.mpeg.MpegContainerProbe;
import lavaplayer.container.ogg.OggContainerProbe;
import lavaplayer.container.playlists.M3uPlaylistContainerProbe;
import lavaplayer.container.playlists.PlainPlaylistContainerProbe;
import lavaplayer.container.playlists.PlsPlaylistContainerProbe;
import lavaplayer.container.wav.WavContainerProbe;

/**
 * Lists currently supported containers and their probes.
 */
public enum MediaContainer {
  WAV(new WavContainerProbe()),
  MKV(new MatroskaContainerProbe()),
  MP4(new MpegContainerProbe()),
  FLAC(new FlacContainerProbe()),
  OGG(new OggContainerProbe()),
  M3U(new M3uPlaylistContainerProbe()),
  PLS(new PlsPlaylistContainerProbe()),
  PLAIN(new PlainPlaylistContainerProbe()),
  MP3(new Mp3ContainerProbe()),
  ADTS(new AdtsContainerProbe());

  /**
   * The probe used to detect files using this container and create the audio tracks for them.
   */
  public final MediaContainerProbe probe;

  MediaContainer(MediaContainerProbe probe) {
    this.probe = probe;
  }
}
