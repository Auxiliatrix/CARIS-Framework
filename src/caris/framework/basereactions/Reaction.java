package caris.framework.basereactions;

import java.net.SocketException;

import caris.configuration.calibration.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.util.RequestBuffer;

public abstract class Reaction extends Thread implements Comparable<Reaction> {
	
	public int priority;
	
	public Reaction() {
		this(0);
	}
	
	public Reaction(int priority) {
		this.priority = priority;
	}
	
	@Override
	public void run() {
		RequestBuffer.request(() -> {
			Brain.threadCount.incrementAndGet();
			for( int f=0; f<Constants.STUBBORNNESS; f++ ) {
				try {
					process();
					Brain.threadCount.decrementAndGet();
					break;
				} catch (SocketException e) {
					try {
						Thread.sleep(Constants.RETRY_SOCKETEXCEPTION_DELAY);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	public abstract void process() throws SocketException;
	
	@Override
	public int compareTo(Reaction r) {
		int compare = r.priority;
		if( this.priority < compare ) {
			return 1;
		} else if( this.priority > compare ) {
			return -1;
		} else {
			return 0;
		}
	}
}
