package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.reactions.ReactionChannelTrack;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;

public class ChannelCreateHandler extends GeneralHandler {

	public ChannelCreateHandler() {
		super("ChannelCreate");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof ChannelCreateEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		ChannelCreateEvent channelCreateEvent = (ChannelCreateEvent) event;
		return new ReactionChannelTrack(channelCreateEvent.getGuild(), channelCreateEvent.getChannel(), -1);
	}

	@Override
	public String getDescription() {
		return "Handles the creation of new channels on " + Constants.NAME + "'s servers.";
	}
	
}
