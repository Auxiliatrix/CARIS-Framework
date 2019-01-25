package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.HelpBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.EmbedReaction;

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
		ArrayList<String> tokens = messageEventWrapper.tokens;
		Handler handler = null;
		if( tokens.size() > 1 ) {
			for( MessageHandler.Access accessLevel : MessageHandler.Access.values() ) {
				if( accessLevel.toString().equalsIgnoreCase(tokens.get(1)) ) {
					return new EmbedReaction(HelpBuilder.getHelpEmbed(accessLevel), messageEventWrapper.getChannel());
				}
			}
			for( String name : Brain.handlers.keySet() ) {
				if( name.equalsIgnoreCase(tokens.get(1)) ) {
					handler = Brain.handlers.get(name);
				}
			}
			if( handler != null ) {
				return new EmbedReaction(HelpBuilder.getHelpEmbed(handler), messageEventWrapper.getChannel());
			} else {
				return new EmbedReaction(ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "That module does not exist!"), messageEventWrapper.getChannel());
			}
		} else {
			return new EmbedReaction(HelpBuilder.getHelpEmbed(), messageEventWrapper.getChannel());
		}
	}

	@Override
	public String getDescription() {
		return "Provides information on how to use " + Constants.NAME + ".";
	}
	
	@Override
	public ArrayList<String> getUsage() {
		ArrayList<String> usage = new ArrayList<String>();
		usage.add(invocation + " [module]");
		return usage;
	}
}
