package caris.framework.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.CreditsBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;

@Module(name = "Credits", root = true)
@Help(
		category = "Developer", 
		description = "Displays credits for " + Constants.NAME + "'s development.", 
		usage = {
					Constants.INVOCATION_PREFIX + "Credits"
				}
	)
public class CreditsHandler extends MessageHandler {

	public CreditsHandler() {
		super();
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		return new MessageReaction(mew.getChannel(), CreditsBuilder.getCreditsEmbed(), 0);
	}
	
}
