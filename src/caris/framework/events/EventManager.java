package caris.framework.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import caris.framework.basehandlers.ScriptModule;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.InteractiveModule;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.NullReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.reactions.SaveDataReaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;

public class EventManager extends SuperEvent {
	
	@EventSubscriber
	@Override
	public void onEvent(Event event) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				List<Reaction> reactions = new ArrayList<Reaction>();
				MultiReaction passiveQueue = new MultiReaction();
				for( Handler handler : Brain.modules.values() ) {
					Reaction reaction = handler.handle(event);
					if( reaction != null && !(reaction instanceof NullReaction) ) {
						if( reaction.priority == -1 ) {
							passiveQueue.add(reaction);
						} else {
							reactions.add(reaction);
						}
					}
				}
				for( InteractiveModule interactive : Brain.interactives ) {
					Reaction reaction = interactive.handle(event);
					if( reaction != null && !(reaction instanceof NullReaction) ) {
						if( reaction.priority == -1 ) {
							passiveQueue.add(reaction);
						} else {
							reactions.add(reaction);
						}
					}
				}
				for( ScriptModule scripts : Brain.scripts.values() ) {
					Reaction reaction = scripts.handle(event);
					if( reaction != null && !(reaction instanceof NullReaction) ) {
						if( reaction.priority == -1 ) {
							passiveQueue.add(reaction);
						} else {
							reactions.add(reaction);
						}
					}
				}
				while( !Brain.cli.isReady() || !Brain.cli.isLoggedIn() ) {
					try {
						Logger.error("Client disconnected. Waiting for reconnect.");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if( !reactions.isEmpty() ) {
					Reaction[] options = new Reaction[reactions.size()];
					for( int f=0; f<reactions.size(); f++ ) {
						options[f] = reactions.get(f);
					}
					Arrays.sort(options);
					options[0].start();
				}
				if( !passiveQueue.isEmpty() || !reactions.isEmpty() ) {
					passiveQueue.add(new SaveDataReaction());
				}

				passiveQueue.start();
			}
		};
		Brain.threadQueue.add(thread);
	}
}
