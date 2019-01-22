package caris.framework.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.main.Brain;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.utilities.StringUtilities;

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
		MessageHandler handler = null;
		if( tokens.size() > 1 ) {
			for( int f=1; f<tokens.size(); f++ ) {
				if( StringUtilities.containsIgnoreCase(Brain.handlers.keySet(), tokens.get(f)) ) {
					Handler temp = Brain.handlers.get(tokens.get(f));
					if( temp instanceof MessageHandler ) {
						handler = (MessageHandler) temp;
					}
				}
			}
		}
		if( handler == null ) {
			return new ReactionEmbed(new HelpBuilder().getEmbeds(), messageEventWrapper.getChannel());
		} else {
			return new ReactionEmbed(new HelpBuilder(handler).getEmbeds(), messageEventWrapper.getChannel());
		}
	}

	@Override
	public String getDescription() {
		return "Provides information on how to use " + Constants.NAME + ".";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(invocation, "Displays basic information on how to use " + Constants.NAME);
		usage.put(invocation + " <Module>", "Displays information on a module");
		return usage;
	}
}
