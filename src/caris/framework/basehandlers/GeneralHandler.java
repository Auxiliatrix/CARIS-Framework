package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

public abstract class GeneralHandler extends Handler {
	
	public GeneralHandler(String name) {
		this(name, false);
	}
	
	public GeneralHandler(String name, boolean allowBots) {
		super(name, allowBots);
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		if( botFilter(event) ) {
			Logger.debug("Event from a bot. Aborting.", 1, true);
			return null;
		} if( isTriggered(event) ) {
			Logger.debug("Conditions satisfied. Processing.", 1, true);
			Reaction result = process(event);
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
	
	protected abstract boolean isTriggered(Event event);
	protected abstract Reaction process(Event event);
	
}
