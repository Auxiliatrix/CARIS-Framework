package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.HearReaction;
import caris.framework.reactions.MessageLogReaction;
import caris.framework.reactions.UpdateUserReaction;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

@Module(name = "MessageLogger", allowBots = true)
public class MessageLoggerHandler extends GeneralHandler<MessageReceivedEvent> {
	
	public MessageLoggerHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(MessageReceivedEvent event) {
		return true;
	}
	
	@Override
	protected Reaction process(MessageReceivedEvent typedEvent) {
		MultiReaction logMessage = new MultiReaction(-1);
		logMessage.add(new HearReaction(typedEvent.getMessage().getFormattedContent(), typedEvent.getAuthor(), typedEvent.getChannel()));
		logMessage.add(new MessageLogReaction(typedEvent.getChannel(), typedEvent.getMessage()));
		logMessage.add(new UpdateUserReaction(typedEvent.getGuild(), typedEvent.getAuthor(), "lastMessage_" + typedEvent.getChannel().getLongID(), typedEvent.getMessage(), true));
		return logMessage;
	}
	
}
