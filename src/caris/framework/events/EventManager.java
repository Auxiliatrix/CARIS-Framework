package caris.framework.events;

import java.util.ArrayList;
import java.util.Arrays;

import caris.framework.basehandlers.Handler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;

public class EventManager extends SuperEvent {

	public int instanceCount = 0;
	
	@EventSubscriber
	@Override
	public void onEvent(Event event) {
		instanceCount++;
		if( instanceCount > 1 ) {
			Logger.error("Number of instances: " + instanceCount);
		}
		Thread thread = new Thread() {
			@Override
			public void run() {
				ArrayList<Reaction> reactions = new ArrayList<Reaction>();
				ArrayList<Reaction> passiveQueue = new ArrayList<Reaction>();
				for( Handler h : Brain.handlers.values() ) {
					Reaction r = h.handle(event);
					if( r != null ) {
						if( r.priority == -1 ) {
							passiveQueue.add(r);
						} else {
							reactions.add(r);
						}
					}
				}
				if( !reactions.isEmpty() ) {
					Reaction[] options = new Reaction[reactions.size()];
					for( int f=0; f<reactions.size(); f++ ) {
						options[f] = reactions.get(f);
					}
					Arrays.sort(options);
					options[0].run();
				}
				MultiReaction passiveQueueExecutor = new MultiReaction(passiveQueue);
				passiveQueueExecutor.run();
			}
		};
		Brain.threadQueue.add(thread);
		instanceCount--;
	}
}
