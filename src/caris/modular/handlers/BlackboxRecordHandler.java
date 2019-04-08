package caris.modular.handlers;

import java.util.List;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

@Module(name = "BlackboxRecord", allowBots = true)
public class BlackboxRecordHandler extends GeneralHandler<MessageReceivedEvent> {

	public BlackboxRecordHandler() {
		super();
	}

	@Override
	protected boolean isTriggered(MessageReceivedEvent typedEvent) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Reaction process(MessageReceivedEvent typedEvent) {
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
