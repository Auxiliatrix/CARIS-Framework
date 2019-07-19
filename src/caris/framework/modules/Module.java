package caris.framework.modules;

import caris.framework.reactions.Reaction;
import sx.blah.discord.api.events.Event;

public abstract class Module<E extends Event> {
	
	public Class<E> eventClass;
	
	public abstract String getName();
	
	public Module( Class<E> eventClass) {
		this.eventClass = eventClass;
	}
	
	public boolean triggered(E event) {
		return false;
	}
	
	public Reaction handle(E event) {
		return null;
	}
	
}
