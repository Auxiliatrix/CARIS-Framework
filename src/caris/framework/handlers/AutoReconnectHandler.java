package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReconnectReaction;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

public class AutoReconnectHandler extends GeneralHandler<DisconnectedEvent> {

	public AutoReconnectHandler() {
		super("AutoReconnect");
	}

	@Override
	protected boolean isTriggered(DisconnectedEvent typedEvent) {
		return true;
	}

	@Override
	protected Reaction process(DisconnectedEvent typedEvent) {
		return new ReconnectReaction();
	}
	
	@Override
	public String getDescription() {
		return "Reconnects to server if disconnected due to inactivity.";
	}
	
}
