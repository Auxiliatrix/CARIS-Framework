package caris.framework.handlers;

import java.time.ZoneId;
import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.embedbuilders.StatusBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;

public class StatusHandler extends MessageHandler {

	public StatusHandler() {
		super("Status", Access.DEVELOPER);
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper);
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		long ping = System.currentTimeMillis() - messageEventWrapper.getMessage().getTimestamp().atZone(ZoneId.of("America/Los_Angeles")).toInstant().toEpochMilli();
		return new MessageReaction(messageEventWrapper.getChannel(), StatusBuilder.getStatusEmbed(ping, Brain.threadQueue.size()));
	}

	@Override
	public String getDescription() {
		return "Gets " + Constants.NAME + "'s current status.";
	}
	

	@Override
	public ArrayList<String> getUsage() {
		ArrayList<String> usage = new ArrayList<String>();
		usage.add(invocation);
		return usage;
	}
	
}
