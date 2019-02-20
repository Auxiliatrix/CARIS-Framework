package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.tokens.Duration;
import caris.framework.utilities.TimeUtilities;
import caris.modular.interactives.PollInteractive;

public class PollHandler extends MessageHandler {
	
	public PollHandler() {
		super("Poll");
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("poll", "question", "vote", "quesitonaire") && messageEventWrapper.quotedTokens.size() > 0;
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		List<String> options = new ArrayList<String>();
		if( messageEventWrapper.quotedTokens.size() > 1 ) {
			for( int f=1; f<messageEventWrapper.quotedTokens.size(); f++ ) {
				options.add(messageEventWrapper.quotedTokens.get(f));
			}
		}
		if( options.size() == 0 ) {
			options.add("Yes");
			options.add("No");
		}
		Duration timeout = TimeUtilities.stringToTime(messageEventWrapper.notQuoted());
		if( timeout.asMili() == 0 ) {
			return new InteractiveCreateReaction(messageEventWrapper.getChannel(), new PollInteractive(messageEventWrapper.quotedTokens.get(0), options.toArray(new String[options.size()]), messageEventWrapper.getAuthor()));
		} else {
			return new InteractiveCreateReaction(messageEventWrapper.getChannel(), new PollInteractive(messageEventWrapper.quotedTokens.get(0), options.toArray(new String[options.size()]), messageEventWrapper.getAuthor(), timeout));
		}
	}

	@Override
	public String getDescription() {
		return "Creates polls that can be voted on.";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add( Constants.NAME + " can we vote on \"are hotdogs a sandwich\" for like two minutes please?");
		usage.add( Constants.NAME + " I wanna make a poll for \"What's the best girl scout cookies\" with options for \"Thin Mints\" and \"S'mores\".");
		return usage;
	}
	
}
