package caris.framework.events;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.Logger;

public class TimedEventManager extends Thread {
	
	@Override
	public void run() {
		while(true) {
			while( !Brain.cli.isReady() || !Brain.cli.isLoggedIn()) {
				try {
					Logger.error("Client disconnected. Waiting for reconnect.");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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
