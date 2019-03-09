package caris.framework.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.TrackChannelReaction;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;

public class TrackChannelHandler extends GeneralHandler<ChannelCreateEvent> {

	public TrackChannelHandler() {
		super("TrackChannel");
	}
	
	@Override
	protected boolean isTriggered(ChannelCreateEvent typedEvent) {
		return true;
	}
	
	@Override
	protected Reaction process(ChannelCreateEvent typedEvent) {
		return new TrackChannelReaction(typedEvent.getGuild(), typedEvent.getChannel(), -1);
	}

	@Override
	public String getDescription() {
		return "Handles the creation of new channels on " + Constants.NAME + "'s servers.";
	}
	
}
