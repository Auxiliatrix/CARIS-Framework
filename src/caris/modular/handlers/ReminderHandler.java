package caris.modular.handlers;

import java.time.ZoneId;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.SetTimedReaction;
import caris.framework.tokens.Duration;
import caris.framework.utilities.TimeUtilities;

public class ReminderHandler extends MessageHandler {

	public ReminderHandler() {
		super("Reminder");
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("remind", "reminder") && messageEventWrapper.containsAnyWords("second", "seconds", "minute", "minutes", "hour", "hours", "day", "days");
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction setReminder = new MultiReaction();
		String message = "";
		String parseable = messageEventWrapper.message;
		if( messageEventWrapper.quotedTokens.size() > 0 ) {
			message = messageEventWrapper.quotedTokens.get(0);
			parseable = messageEventWrapper.message.replace("\"" + messageEventWrapper.quotedTokens.get(0) + "\"", "");
		}
		if( message.isEmpty() ) {
			message = messageEventWrapper.getAuthor().mention() + ", here's your reminder!";
		} else {
			message = messageEventWrapper.getAuthor().mention() + ", here's your reminder to \"" + message + "\"!";
		}
		try {
			Duration timer = TimeUtilities.stringToTime(parseable);
			setReminder.add(new SetTimedReaction(new MessageReaction(messageEventWrapper.getChannel(), message), timer, messageEventWrapper.getMessage().getTimestamp().atZone(ZoneId.systemDefault()).toEpochSecond()));
			setReminder.add(new MessageReaction(messageEventWrapper.getChannel(), "Reminder set successfully!"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			setReminder.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "Couldn't parse the given time!")));
		}
		return setReminder;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public List<String> getUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
