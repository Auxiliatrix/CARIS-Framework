package caris.framework.handlers;

import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.HelpBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;

@Module(name = "Help")
@Help(category = "Default", description = "Provides information on how to use " + Constants.NAME + ".", usage = {Constants.INVOCATION_PREFIX + "Help" + " <category>", Constants.INVOCATION_PREFIX + "Help + <module>"})
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
		if( tokens.size() > 1 ) {
			for( String category : Handler.categories ) {
				if( category.equalsIgnoreCase(tokens.get(1)) ) {
					return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed(category));
				}
			}
			for( String name : Brain.modules.keySet() ) {
				if( name.equalsIgnoreCase(tokens.get(1)) ) {
					return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed(Brain.modules.get(name)));
				}
			}
			return new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "That module does not exist!"));
		} else {
			return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed());
		}
	}
	
}
