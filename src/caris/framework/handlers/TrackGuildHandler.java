package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.TrackGuildReaction;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;

@Module(name = "TrackGuild", root = true)
public class TrackGuildHandler extends GeneralHandler<GuildCreateEvent> {

	public TrackGuildHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(GuildCreateEvent typedEvent) {
		return true;
	}
	
	@Override
	protected Reaction process(GuildCreateEvent typedEvent) {
		return new TrackGuildReaction(typedEvent.getGuild(), -1);
	}
	
}
