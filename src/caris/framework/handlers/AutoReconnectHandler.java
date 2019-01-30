package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReconnectReaction;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

public class AutoReconnectHandler extends Handler {

	public AutoReconnectHandler() {
		super("AutoReconnect");
	}

	@Override
	public Reaction handle(Event event) {
		if( event instanceof DisconnectedEvent ) {
			return new ReconnectReaction();
		} else {
			return null;
		}
	}

	@Override
	public String getDescription() {
		return "Reconnects to server if disconnected due to inactivity.";
	}
	
}
