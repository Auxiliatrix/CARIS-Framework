package caris.framework.handlers;

import java.time.ZoneId;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.embedbuilders.StatusBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;

@Module(name = "Status")
@Help(
		category = "Developer", 
		description = "Gets " + Constants.NAME + "'s current status.", 
		usage = {
					Constants.INVOCATION_PREFIX + "Status", Constants.NAME + ", what's your status?"
				}
	)
public class StatusHandler extends MessageHandler {

	public StatusHandler() {
		super();
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper) || mentioned(messageEventWrapper) && messageEventWrapper.containsWord("status");
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		long ping = System.currentTimeMillis() - messageEventWrapper.getMessage().getTimestamp().atZone(ZoneId.systemDefault()).toEpochSecond()*1000;
		return new MessageReaction(messageEventWrapper.getChannel(), StatusBuilder.getStatusEmbed(ping), invoked(messageEventWrapper) ? 1 : 2);
	}
	
}
