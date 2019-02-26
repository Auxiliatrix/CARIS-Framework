package caris.framework.events;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;

public class TimedEventManager extends Thread {
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				for( Reaction reaction : Brain.timedQueue.keySet() ) {
					if( System.currentTimeMillis() >= Brain.timedQueue.get(reaction) ) {
						reaction.start();
						Brain.timedQueue.remove(reaction);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
