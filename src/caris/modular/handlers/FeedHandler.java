package caris.modular.handlers;

import com.mashape.unirest.http.Unirest;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.utilities.Verifier;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "Feed")
@Help(
		category = "frc",
		description = "Creates live feeds to stream TBA data.",
		usage = {
					Constants.INVOCATION_PREFIX + "Feed queue <event_key> <team_number>",	// TODO
					Constants.INVOCATION_PREFIX + "Feed results <event_key>",				// TODO
					Constants.INVOCATION_PREFIX + "Feed rankings <event_key>"				// TODO
				}
	)
public class FeedHandler extends MessageHandler {

	public FeedHandler() {
		super(Permissions.MANAGE_CHANNELS);
	}

	@Override
	public Reaction onStartup() {
		return new ReactionRunnable(new Runnable() {
			@Override
			public void run() {
				Unirest.setDefaultHeader("X-TBA-Auth-Key", "Y5RuxdZwUBRRXOOSQ5xG6bZts1rSS8u5vpdjpbFWTqyIpyzSnDf6FWsYABfbNPcB");
			}
		});
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		MultiReaction feedReaction = new MultiReaction(0);
		Verifier feedVerifier = new Verifier("");
		/*
		JSONArray queueArray = null;
		if( messageEventWrapper.tokens.get(2).equals("test") ) {
			queueArray = new JSONArray(TestDataString.getTestData());
		} else {
			queueArray = APIRetriever.getJSONArray(TBA_ENDPOINT + "event/" + messageEventWrapper.tokens.get(2) + "/matches");
		}
		if( queueArray != null ) {
			EmbedObject[] pages = TBABuilder.paginate(queueArray, messageEventWrapper.tokens.get(3));
			if( pages != null ) {
				tbaReaction.add(new InteractiveCreateReaction(messageEventWrapper.getChannel(), new PagedInteractive(pages)));
			} else {
				tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.EXECUTION, "No matches found!")));
			}
		} else {
			tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "You must specify a valid event key!")));
		}
		*/
		return feedReaction;
	}
	
}
