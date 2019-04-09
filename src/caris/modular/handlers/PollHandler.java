package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import caris.framework.tokens.Duration;
import caris.framework.utilities.TimeUtilities;
import caris.modular.interactives.PollInteractive;

@Module(name = "Poll")
@Help(
		category = "Default",
		description = "Creates polls that can be voted on.",
		usage = {
					Constants.NAME + " can we vote on \"are hotdogs a sandwich\" for like two minutes please?",
					Constants.NAME + " I wanna make a poll for \"What's the best girl scout cookies\" with options for \"Thin Mints\" and \"S'mores\"."
				}
	)
public class PollHandler extends MessageHandler {
	
	public PollHandler() {
		super();
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return mentioned(mew) && mew.containsAnyWords("poll", "question", "vote", "quesitonaire") && mew.quotedTokens.size() > 0;
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		List<String> options = new ArrayList<String>();
		
		if( mew.quotedTokens.size() > 10 ) {
			return new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorType.SYNTAX, "You can't have more than 9 options!"));
		} else if( mew.quotedTokens.size() > 1 ) {
			for( int f=1; f<mew.quotedTokens.size(); f++ ) {
				options.add(mew.quotedTokens.get(f));
			}
		} else if( mew.quotedTokens.size() == 1 ) {
			options.add("Yes");
			options.add("No");
		} else {
			return new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorType.SYNTAX, "You must specify a question in quotes!"));
		}
		
		Duration timeout = TimeUtilities.stringToTime(mew.notQuoted());
		if( timeout.asMili() == 0 ) {
			return new InteractiveCreateReaction(mew.getChannel(), new PollInteractive(mew.quotedTokens.get(0), options.toArray(new String[options.size()]), mew.getAuthor()));
		} else {
			return new InteractiveCreateReaction(mew.getChannel(), new PollInteractive(mew.quotedTokens.get(0), options.toArray(new String[options.size()]), mew.getAuthor(), timeout));
		}
	}
	
}
