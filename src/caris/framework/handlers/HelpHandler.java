package caris.framework.handlers;

import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.Verifier;
import caris.framework.utilities.Verifier.Verification;

@Module(name = "Help")
@Help(
		category = "Default", 
		description = "Provides information on how to use " + Constants.NAME + ".", 
		usage = {
					Constants.INVOCATION_PREFIX + "Help <category>", 
					Constants.INVOCATION_PREFIX + "Help <module>"
				}
		)
public class HelpHandler extends MessageHandler {

	public HelpHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper);
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		List<String> tokens = messageEventWrapper.tokens;
		Verifier helpVerifier = new Verifier("invocation", "module or category");
		Verification v = helpVerifier.verify(tokens);
		if( v.pass ) {
			if( StringUtilities.containsIgnoreCase(Handler.categories, v.get(1)) ) {
				return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed(v.get(1)));
			} else if( Brain.modules.keySet().contains(v.get(1).toLowerCase()) ) {
				return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed(Brain.modules.get(v.get(1).toLowerCase())));
			} else {
				return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed());
			}
		} else {
			return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed());
		}
	}
}
