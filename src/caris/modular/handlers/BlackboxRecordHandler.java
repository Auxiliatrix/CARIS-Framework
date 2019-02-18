package caris.modular.handlers;

import java.util.List;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class BlackboxRecordHandler extends GeneralHandler<MessageReceivedEvent> {

	public BlackboxRecordHandler() {
		super("BlackboxRecord", true);
	}

	@Override
	public String getDescription() {
		return "Records messages into blackboxes when active";
	}

	@Override
	protected boolean isTriggered(MessageReceivedEvent typedEvent) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Reaction process(MessageReceivedEvent typedEvent) {
		// And here, you can see where my framework utterly crumbles apart.
		if( Brain.variables.getChannelInfo(typedEvent.getMessage()).channelData.has("blackbox") ) {
			return new ReactionRunnable(new Runnable() {
				@Override
				public void run() {
					((List<Long>) Brain.variables.getChannelInfo(typedEvent.getMessage()).channelData.get("blackbox")).add(typedEvent.getMessage().getLongID());
				}
			}, -1);
		}
		return null;
	}
	
}
