package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

public abstract class GeneralHandler<T extends Event> extends Handler {
	
	public GeneralHandler(String name) {
		this(name, false);
	}
	
	public GeneralHandler(String name, boolean allowBots) {
		super(name, allowBots);
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		if( !eventMatch(event) ) {
			Logger.debug("Event type mismatch. Aborting.", 1, true);
			return null;
		}
		@SuppressWarnings("unchecked")
		T typedEvent = (T) event;
		if( botFilter(event) ) {
			Logger.debug("Event from a bot. Aborting.", 1, true);
			return null;
		} if( isTriggered(typedEvent) ) {
			Logger.debug("Conditions satisfied for " + name + ". Processing.", 1);
			Reaction result = process(typedEvent);
			if( result == null ) {
				Logger.debug("No Reaction produced. Aborting", 1, true);
			} else {
				Logger.debug("Reaction produced from " + name + ". Adding to queue." , 1);
			}
			return result;
		} else {
			Logger.debug("Conditions unsatisfied. Aborting.", 1, true);
			return null;
		}
	}
	
	private boolean eventMatch(Event event) {
		// Checks if event is an instance of parameterized T, even at runtime, via a try-catch.
		// This is why I hate Java, btw.
		try {
			@SuppressWarnings({ "unused", "unchecked" })
			T tEvent = (T) event;
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	protected abstract boolean isTriggered(T typedEvent);
	protected abstract Reaction process(T typedEvent);
	
}
