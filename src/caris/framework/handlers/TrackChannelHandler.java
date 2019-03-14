package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.TrackChannelReaction;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;

@Module(name = "TrackChannel", root = true)
public class TrackChannelHandler extends GeneralHandler<ChannelCreateEvent> {

	public TrackChannelHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(ChannelCreateEvent typedEvent) {
		return true;
	}
	
	@Override
	protected Reaction process(ChannelCreateEvent typedEvent) {
		return new TrackChannelReaction(typedEvent.getGuild(), typedEvent.getChannel(), -1);
	}
	
}
