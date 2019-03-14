package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

public abstract class GeneralHandler<T extends Event> extends Handler {
	
	public GeneralHandler() {
		super();
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		@SuppressWarnings("unchecked")
		T typedEvent = (T) event;
		if( botFilter(event) ) {
			Logger.debug("Event from a bot. Aborting.", 1, true);
			return null;
		}
		if( disableFilter(event) ) {
			Logger.debug("Handler disabled for this location. Aborting.", 1, true);
			return null;
		}
		try {
			if( isTriggered(typedEvent) ) {
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
		} catch (ClassCastException e) {
			Logger.debug("Event mismatch. Aborting.", 1, true);
			return null;
		}
	}
	
	protected abstract boolean isTriggered(T typedEvent);
	protected abstract Reaction process(T typedEvent);
	
}
