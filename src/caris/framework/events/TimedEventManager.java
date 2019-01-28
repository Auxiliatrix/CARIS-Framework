package caris.framework.events;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;

public class TimedEventManager extends Thread {
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				for( Long time : Brain.timedQueue.keySet() ) {
					if( System.currentTimeMillis() >= time ) {
						for( Reaction reaction : Brain.timedQueue.get(time) ) {
							reaction.start();
						}
						Brain.timedQueue.remove(time);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
