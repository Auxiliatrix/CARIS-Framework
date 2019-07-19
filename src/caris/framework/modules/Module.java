package caris.framework.modules;

import caris.framework.reactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

public abstract class Module<E extends Event> {
	
	public Class<E> eventClass;
	
	public abstract String getName();
	
	protected Logger logger;
	
	public Module( Class<E> eventClass ) {
		this.eventClass = eventClass;
		logger = new Logger().addOrigin(getName());
	}
	
	public abstract boolean triggered(E event);	
	public abstract Reaction process(E event);
	
	public Reaction handle(E event) {
		if( triggered(event) ) {
			return process(event);
		} else {
			return null;
		}
	}
	
}
