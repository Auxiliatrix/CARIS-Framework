package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.reactions.HearReaction;
import caris.framework.reactions.MessageLogReaction;
import caris.framework.reactions.UpdateUserReaction;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class MessageLoggerHandler extends GeneralHandler {
	
	public MessageLoggerHandler() {
		super("MessageLogger", true);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof MessageReceivedEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		MultiReaction logMessage = new MultiReaction(-1);
		logMessage.add(new HearReaction(messageReceivedEvent.getMessage().getFormattedContent(), messageReceivedEvent.getAuthor(), messageReceivedEvent.getChannel()));
		logMessage.add(new MessageLogReaction(messageReceivedEvent.getChannel(), messageReceivedEvent.getMessage()));
		logMessage.add(new UpdateUserReaction(messageReceivedEvent.getGuild(), messageReceivedEvent.getAuthor(), "lastMessage_" + messageReceivedEvent.getChannel().getLongID(), messageReceivedEvent.getMessage(), true));
		return logMessage;
	}
	
	@Override
	public String getDescription() {
		return "Logs messages being sent in " + Constants.NAME + "'s servers.";
	}
	
}
