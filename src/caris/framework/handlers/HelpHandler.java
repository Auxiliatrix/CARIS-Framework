package caris.framework.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.HelpBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;

public class HelpHandler extends MessageHandler {

	public HelpHandler() {
		super("Help");
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper);
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		List<String> tokens = messageEventWrapper.tokens;
		Handler handler = null;
		if( tokens.size() > 1 ) {
			for( String category : MessageHandler.categories ) {
				if( category.equalsIgnoreCase(tokens.get(1)) ) {
					return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed(category));
				}
			}
			for( String name : Brain.handlers.keySet() ) {
				if( name.equalsIgnoreCase(tokens.get(1)) ) {
					handler = Brain.handlers.get(name);
				}
			}
			if( handler != null ) {
				return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed(handler));
			} else {
				return new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "That module does not exist!"));
			}
		} else {
			return new MessageReaction(messageEventWrapper.getChannel(), HelpBuilder.getHelpEmbed());
		}
	}

	@Override
	public String getDescription() {
		return "Provides information on how to use " + Constants.NAME + ".";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add(invocation + " [module]");
		return usage;
	}
}
