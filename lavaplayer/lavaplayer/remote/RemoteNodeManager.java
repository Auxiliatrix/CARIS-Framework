package lavaplayer.remote;

import static lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;

import lavaplayer.player.AudioPlayer;
import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.player.event.AudioEventAdapter;
import lavaplayer.tools.DaemonThreadFactory;
import lavaplayer.tools.ExecutorTools;
import lavaplayer.tools.FriendlyException;
import lavaplayer.tools.io.HttpInterfaceManager;
import lavaplayer.track.AudioTrack;
import lavaplayer.track.AudioTrackEndReason;
import lavaplayer.track.InternalAudioTrack;
import lavaplayer.track.playback.AudioTrackExecutor;

/**
 * Manager of remote nodes for audio processing.
 */
public class RemoteNodeManager extends AudioEventAdapter implements RemoteNodeRegistry, Runnable {
  private final DefaultAudioPlayerManager playerManager;
  private final HttpInterfaceManager httpInterfaceManager;
  private final List<RemoteNodeProcessor> processors;
  private final AbandonedTrackManager abandonedTrackManager;
  private final AtomicBoolean enabled;
  private final Object lock;
  private volatile ScheduledThreadPoolExecutor scheduler;
  private volatile List<RemoteNodeProcessor> activeProcessors;

  /**
   * @param playerManager Audio player manager
   */
  public RemoteNodeManager(DefaultAudioPlayerManager playerManager) {
    this.playerManager = playerManager;
    this.httpInterfaceManager = RemoteNodeProcessor.createHttpInterfaceManager();
    this.processors = new ArrayList<>();
    this.abandonedTrackManager = new AbandonedTrackManager();
    this.enabled = new AtomicBoolean();
    this.lock = new Object();
    this.activeProcessors = new ArrayList<>();
  }

  /**
   * Enable and initialise the remote nodes.
   * @param nodeAddresses Addresses of remote nodes
   */
  public void initialise(List<String> nodeAddresses) {
    synchronized (lock) {
      if (enabled.compareAndSet(false, true)) {
        startScheduler(nodeAddresses.size() + 1);
      } else {
        scheduler.setCorePoolSize(nodeAddresses.size() + 1);
      }

      List<String> newNodeAddresses = new ArrayList<>(nodeAddresses);

      for (Iterator<RemoteNodeProcessor> iterator = processors.iterator(); iterator.hasNext(); ) {
        RemoteNodeProcessor processor = iterator.next();

        if (!newNodeAddresses.remove(processor.getNodeAddress())) {
          processor.shutdown();
          iterator.remove();
        }
      }

      for (String nodeAddress : newNodeAddresses) {
        RemoteNodeProcessor processor = new RemoteNodeProcessor(playerManager, nodeAddress, scheduler,
            httpInterfaceManager, abandonedTrackManager);

        scheduler.submit(processor);
        processors.add(processor);
      }

      activeProcessors = new ArrayList<>(processors);
    }
  }

  /**
   * Shut down, freeing all threads and stopping all tracks executed on remote nodes.
   * @param terminal True if initialise will never be called again.
   */
  public void shutdown(boolean terminal) {
    synchronized (lock) {
      if (!enabled.compareAndSet(true, false)) {
        return;
      }

      ExecutorTools.shutdownExecutor(scheduler, "node manager");

      for (RemoteNodeProcessor processor : processors) {
        processor.processHealthCheck(true);
      }

      abandonedTrackManager.shutdown();

      processors.clear();
      activeProcessors = new ArrayList<>(processors);
    }

    if (terminal) {
      IOUtils.closeQuietly(httpInterfaceManager);
    }
  }

  @Override
  public boolean isEnabled() {
    return enabled.get();
  }

  /**
   * Start playing an audio track remotely.
   * @param remoteExecutor The executor of the track
   */
  public void startPlaying(RemoteAudioTrackExecutor remoteExecutor) {
    RemoteNodeProcessor processor = getNodeForNextTrack();

    processor.startPlaying(remoteExecutor);
  }

  private void startScheduler(int initialSize) {
    ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(initialSize, new DaemonThreadFactory("remote"));
    scheduledExecutor.scheduleAtFixedRate(this, 2000, 2000, TimeUnit.MILLISECONDS);
    scheduler = scheduledExecutor;
  }

  private RemoteNodeProcessor getNodeForNextTrack() {
    int lowestPenalty = Integer.MAX_VALUE;
    RemoteNodeProcessor node = null;

    for (RemoteNodeProcessor processor : processors) {
      int penalty = processor.getBalancerPenalty();

      if (penalty < lowestPenalty) {
        lowestPenalty = penalty;
        node = processor;
      }
    }

    if (node == null) {
      throw new FriendlyException("No available machines for playing track.", SUSPICIOUS, null);
    }

    return node;
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    AudioTrackExecutor executor = ((InternalAudioTrack) track).getActiveExecutor();

    if (endReason != AudioTrackEndReason.FINISHED && executor instanceof RemoteAudioTrackExecutor) {
      for (RemoteNodeProcessor processor : activeProcessors) {
        processor.trackEnded((RemoteAudioTrackExecutor) executor, true);
      }
    }
  }

  @Override
  public void run() {
    for (RemoteNodeProcessor processor : activeProcessors) {
      processor.processHealthCheck(false);
    }

    abandonedTrackManager.drainExpired();
  }

  @Override
  public RemoteNode getNodeUsedForTrack(AudioTrack track) {
    for (RemoteNodeProcessor processor : activeProcessors) {
      if (processor.isPlayingTrack(track)) {
        return processor;
      }
    }

    return null;
  }

  @Override
  public List<RemoteNode> getNodes() {
    return new ArrayList<>(activeProcessors);
  }
}
