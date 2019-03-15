package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.mashape.unirest.http.Unirest;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.SetTimedReaction;
import caris.framework.utilities.Verifier;
import caris.framework.utilities.Verifier.Verification;
import caris.modular.reactions.TBAMatchAlertReaction;
import caris.modular.reactions.TBAMatchTimeUpdateReaction;
import caris.modular.tokens.TBAMatchObject;
import caris.modular.utilities.APIRetriever;
import caris.modular.utilities.TBAObjectFactory;
import caris.modular.utilities.TestDataString;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "Feed")
@Help(
		category = "FRC",
		description = "Creates live feeds to stream TBA data.",
		usage = {
					Constants.INVOCATION_PREFIX + "Feed queue <event_key> <team_number> [mentions...]",	// TODO
				}
	)
public class FeedHandler extends MessageHandler {

	public static final String TBA_ENDPOINT = "https://www.thebluealliance.com/api/v3/";
	public static final int ALERT_SECONDS_BEFORE = 30;
	
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
		Verifier initialVerifier = new Verifier("command", "option", "event key").withValidaters(null, x -> ((String) x).equalsIgnoreCase("queue"), null);
		Verification initialVerification = initialVerifier.verify(mew.tokens);
		if( initialVerification.pass ) {
			if( initialVerification.get(1).equalsIgnoreCase("queue") ) {
				Verifier queueVerifier = new Verifier("command", "option", "event key", "team number");
				Verification queueVerification = queueVerifier.verify(mew.tokens);
				if( queueVerification.pass ) {
					JSONArray queueArray = null;
					if( initialVerification.get(2).equals("test") ) {
						queueArray = new JSONArray(TestDataString.getTestData());
					} else {
						queueArray = APIRetriever.getJSONArray(TBA_ENDPOINT + "event/" + initialVerification.get(2) + "/matches");
					}
					if( queueArray != null ) {
						TBAMatchObject[] queue = TBAObjectFactory.generateTBAMatchQueue(queueArray, queueVerification.get(3));
						List<TBAMatchObject> futureQueueList = new ArrayList<TBAMatchObject>();
						for( TBAMatchObject match : queue ) {
							if( match.predictedTime*1000 > System.currentTimeMillis() ) {
								futureQueueList.add(match);
							}
						}
						if( futureQueueList.size() > 0 ) {
							TBAMatchObject[] futureQueue = futureQueueList.toArray(new TBAMatchObject[futureQueueList.size()]);
							feedReaction.add(new SetTimedReaction(new TBAMatchAlertReaction(mew.getChannel(), futureQueue, queueVerification.get(3), mew.getAllMentionedUsers()), (futureQueue[0].predictedTime-ALERT_SECONDS_BEFORE)*1000));
							feedReaction.add(new SetTimedReaction(new TBAMatchTimeUpdateReaction(queueVerification.get(2), queueVerification.get(3)), System.currentTimeMillis()+1000));
							feedReaction.add(new MessageReaction(mew.getChannel(), "Alert Queue Set!"));
						} else {
							feedReaction.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.EXECUTION, "All matches complete!")));
						}
					} else {
						feedReaction.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorType.KEYWORD, "You must specify a valid event key!")));
					}
				} else {
					feedReaction.add(new MessageReaction(mew.getChannel(), queueVerification.getErrorEmbed()));
				}
			}
		} else {
			feedReaction.add(new MessageReaction(mew.getChannel(), initialVerification.getErrorEmbed()));
		}
		return feedReaction;
	}
	
}