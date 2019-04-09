package caris.framework.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.interactives.PagedInteractive;
import caris.framework.main.Brain;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.Verifier;
import caris.framework.utilities.Verifier.Verification;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

@Module(name = "Help", root = true)
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
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}
	
	@Override
	protected Reaction process(MessageEventWrapper mew) {
		Verifier helpVerifier = new Verifier("invocation", "module or category");
		Verification v = helpVerifier.verify(mew.tokens);
		EmbedObject[] helpPages = null;
		if( v.pass ) {
			if( StringUtilities.containsIgnoreCase(Handler.categories, v.get(1)) ) {
				helpPages = HelpBuilder.getHelpEmbed(v.get(1), mew.getGuild());
			} else if( Brain.modules.keySet().contains(v.get(1).toLowerCase()) ) {
				helpPages = HelpBuilder.getHelpEmbed(Brain.modules.get(v.get(1).toLowerCase()), mew.getGuild());
			}
		}
		if( helpPages == null ) {
			helpPages = HelpBuilder.getHelpEmbed();
		}
		if( helpPages.length == 1 ) {
			return new MessageReaction(mew.getChannel(), helpPages[0]);
		} else {
			return new InteractiveCreateReaction(mew.getChannel(), new PagedInteractive(helpPages));
		}
	}
}
