package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.reactions.ReactionGuildTrack;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;

public class GuildCreateHandler extends GeneralHandler {

	public GuildCreateHandler() {
		super("GuildCreate", false);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof GuildCreateEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		GuildCreateEvent guildCreateEvent = (GuildCreateEvent) event;
		return new ReactionGuildTrack(guildCreateEvent.getGuild(), -1);
	}

	@Override
	public String getDescription() {
		return "Handles the creation of guilds on " + Constants.NAME + "'s servers.";
	}
	
}
