package caris.framework.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basehandlers.MessageHandler.Command;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.embedbuilders.TimedQueueBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.interactives.PagedInteractive;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "TimeStatus", whitelist = true)
@Help(
		category = "Developer",
		description = "Displays the status of " + Constants.NAME + "'s timed queue.",
		usage = {
					Constants.INVOCATION_PREFIX + "TimeStatus"
				}
	)
@Command(aliases = {"TS"})
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
		EmbedObject[] timePages = TimedQueueBuilder.getTimedQueue();
		if( timePages.length == 1 ) {
			return new MessageReaction(mew.getChannel(), timePages[0]);
		} else {
			return new InteractiveCreateReaction(mew.getChannel(), new PagedInteractive(timePages));
		}
	}

}
