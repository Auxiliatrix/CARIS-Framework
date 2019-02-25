package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.reactions.TrackGuildReaction;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;

public class TrackGuildHandler extends GeneralHandler<GuildCreateEvent> {

	public TrackGuildHandler() {
		super("TrackGuild");
	}
	
	@Override
	protected boolean isTriggered(GuildCreateEvent typedEvent) {
		return true;
	}
	
	@Override
	protected Reaction process(GuildCreateEvent typedEvent) {
		return new TrackGuildReaction(typedEvent.getGuild(), -1);
	}

	@Override
	public String getDescription() {
		return "Handles the creation of guilds on " + Constants.NAME + "'s servers.";
	}
	
}
