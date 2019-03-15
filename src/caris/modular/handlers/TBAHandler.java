package caris.modular.handlers;

import org.json.JSONArray;

import com.mashape.unirest.http.Unirest;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.interactives.PagedInteractive;
import caris.framework.main.Brain;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import caris.framework.utilities.Verifier;
import caris.framework.utilities.Verifier.Verification;
import caris.modular.embedbuilders.TBABuilder;
import caris.modular.utilities.APIRetriever;
import caris.modular.utilities.TestDataString;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

@Module(name = "TBA")
@Help(
		category = "FRC",
		description = "Reads from thebluealliance website to provide online data.",
		usage = {
					Constants.INVOCATION_PREFIX + "TBA matches <event_key>",
					Constants.INVOCATION_PREFIX + "TBA queue <event_key> <team_number>",
					Constants.INVOCATION_PREFIX + "TBA team <team_number> [event_key]" // TODO
				}
	)
public class TBAHandler extends MessageHandler {

	public static final String TBA_ENDPOINT = "https://www.thebluealliance.com/api/v3/";
	
	public TBAHandler() {
		super();
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
		MultiReaction tbaReaction = new MultiReaction(0);
		Verifier initialVerifier = new Verifier("command", "option").withValidaters(null, x -> ((String) x).equalsIgnoreCase("matches") || ((String) x).equalsIgnoreCase("queue"));
		Verification initialVerification = initialVerifier.verify(mew.tokens);
		if( initialVerification.pass ) {
			if( initialVerification.get(1).equalsIgnoreCase("matches") ) {
				Verifier matchesVerifier = new Verifier("command", "option", "event key");
				Verification matchesVerification = matchesVerifier.verify(mew.tokens);
				if( matchesVerification.pass ) {
					JSONArray queueArray = null;
					if( matchesVerification.get(2).equals("test") ) {
						queueArray = new JSONArray(TestDataString.getTestData());
					} else {
						queueArray = APIRetriever.getJSONArray(TBA_ENDPOINT + "event/" + matchesVerification.get(2) + "/matches");
					}
					if( queueArray != null ) {
						int offset = 0;
						if( Brain.variables.getGuildInfo(mew.getGuild()).guildData.has("time_zone") ) {
							offset = Brain.variables.getGuildInfo(mew.getGuild()).guildData.getInt("time_zone");
						}
						EmbedObject[] pages = TBABuilder.paginate(queueArray, offset);
						if( pages != null ) {
							tbaReaction.add(new InteractiveCreateReaction(mew.getChannel(), new PagedInteractive(pages)));
						} else {
							tbaReaction.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "No matches found!")));
						}
					} else {
						tbaReaction.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "You must specify a valid event key!")));
					}
				} else {
					tbaReaction.add(new MessageReaction(mew.getChannel(), matchesVerification.getErrorEmbed()));
				}
			} else if( initialVerification.get(1).equalsIgnoreCase("queue") ) {
				Verifier queueVerifier = new Verifier("command", "option", "event key", "team number");
				Verification queueVerification = queueVerifier.verify(mew.tokens);
				if( queueVerification.pass ) {
					JSONArray queueArray = null;
					if( mew.tokens.get(2).equals("test") ) {
						queueArray = new JSONArray(TestDataString.getTestData());
					} else {
						queueArray = APIRetriever.getJSONArray(TBA_ENDPOINT + "event/" + queueVerification.get(2) + "/matches");
					}
					if( queueArray != null ) {
						int offset = 0;
						if( Brain.variables.getGuildInfo(mew.getGuild()).guildData.has("time_zone") ) {
							offset = Brain.variables.getGuildInfo(mew.getGuild()).guildData.getInt("time_zone");
						}
						EmbedObject[] pages = TBABuilder.paginate(queueArray, queueVerification.get(3), offset);
						if( pages != null ) {
							tbaReaction.add(new InteractiveCreateReaction(mew.getChannel(), new PagedInteractive(pages)));
						} else {
							tbaReaction.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.EXECUTION, "No matches found!")));
						}
					} else {
						tbaReaction.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "You must specify a valid event key!")));
					}
				} else {
					tbaReaction.add(new MessageReaction(mew.getChannel(), queueVerification.getErrorEmbed()));
				}
			}
		} else {
			tbaReaction.add(new MessageReaction(mew.getChannel(), initialVerification.getErrorEmbed()));
		}
		return tbaReaction;
	}
	
}
