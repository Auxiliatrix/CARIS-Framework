package caris.framework.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.CreditsBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;

public class CreditsHandler extends MessageHandler {

	public CreditsHandler() {
		super("Credits");
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper);
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		return new MessageReaction(messageEventWrapper.getChannel(), CreditsBuilder.getCreditsEmbed(), 0);
	}

	@Override
	public String getDescription() {
		return "Displays credits for " + Constants.NAME + "'s development.";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add(invocation);
		return usage;
	}
	
}
