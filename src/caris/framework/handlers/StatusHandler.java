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
					Constants.INVOCATION_PREFIX + "Status"
				}
	)
public class StatusHandler extends MessageHandler {

	public StatusHandler() {
		super();
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		long ping = System.currentTimeMillis() - mew.getMessage().getTimestamp().atZone(ZoneId.systemDefault()).toEpochSecond()*1000;
		return new MessageReaction(mew.getChannel(), StatusBuilder.getStatusEmbed(ping), invoked(mew) ? 1 : 2);
	}
	
}
