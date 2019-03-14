package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReconnectReaction;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

@Module(name = "AutoReconnect", root = true)
public class AutoReconnectHandler extends GeneralHandler<DisconnectedEvent> {

	public AutoReconnectHandler() {
		super();
	}

	@Override
	protected boolean isTriggered(DisconnectedEvent typedEvent) {
		return true;
	}

	@Override
	protected Reaction process(DisconnectedEvent typedEvent) {
		return new ReconnectReaction();
	}
	
}
