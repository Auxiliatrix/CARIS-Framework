package caris.framework.modules;

import caris.framework.reactions.Reaction;
import sx.blah.discord.api.events.Event;

public interface Module<E extends Event> {
	
	public String name = "";
	public int priority = -1;
	
	public boolean triggered(E event);
	public Reaction handle(E event);
	
}
