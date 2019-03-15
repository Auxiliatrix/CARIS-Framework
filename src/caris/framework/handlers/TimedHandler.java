package caris.framework.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.TimedQueueBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "TimeStatus", whitelist = true)
@Help(
		category = "Developer",
		description = "Displays the status of " + Constants.NAME + "'s timed queue.",
		usage = {
					Constants.INVOCATION_PREFIX + "TimeStatus"
				}
	)
public class TimedHandler extends MessageHandler {
	
	public TimedHandler() {
		super(Permissions.ADMINISTRATOR);
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		return new MessageReaction(mew.getChannel(), TimedQueueBuilder.getTimedQueue());
	}

}
