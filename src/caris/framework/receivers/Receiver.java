package caris.framework.receivers;
import java.util.List;

import caris.framework.main.Brain;
import caris.framework.modules.Module;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;

public class Receiver<E extends Event> {

	private List<Module> handlers;
	
	public Receiver( List<Module> handlers ) {
		this.handlers = handlers;
	}
	
	@EventSubscriber
	public void onReceive( E event ) {
		for( Module h : handlers ) {
			
		}
	}
	
}
