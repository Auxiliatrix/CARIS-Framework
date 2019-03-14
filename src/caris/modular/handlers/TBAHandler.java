package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.mashape.unirest.http.Unirest;

import caris.framework.basehandlers.Handler.Module;
import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.interactives.PagedInteractive;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.SetTimedReaction;
import caris.modular.embedbuilders.TBABuilder;
import caris.modular.reactions.TBAMatchAlertReaction;
import caris.modular.reactions.TBAMatchTimeUpdateReaction;
import caris.modular.tokens.TBAMatchObject;
import caris.modular.utilities.APIRetriever;
import caris.modular.utilities.TBAObjectFactory;
import caris.modular.utilities.TestDataString;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

@Module(name = "TBA")
@Help(
		category = "frc",
		description = "Reads from thebluealliance website to provide live tournament data.",
		usage = {
					Constants.INVOCATION_PREFIX + "TBA matches <event_key>",
					Constants.INVOCATION_PREFIX + "TBA queue <event_key> <team_number>",
					Constants.INVOCATION_PREFIX + "TBA alert <event_key> <team_number> <@user.. @roles..>"
				}
	)
public class TBAHandler extends MessageHandler {

	public static final String TBA_ENDPOINT = "https://www.thebluealliance.com/api/v3/";
	public static final int ALERT_SECONDS_BEFORE = 300;
	
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
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper);
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction tbaReaction = new MultiReaction(0);
		if( messageEventWrapper.tokens.size() > 1 ) {
			if( messageEventWrapper.tokens.get(1).equalsIgnoreCase("matches") ) {
				if( messageEventWrapper.tokens.size() > 2 ) {
					JSONArray queueArray = null;
					if( messageEventWrapper.tokens.get(2).equals("test") ) {
						queueArray = new JSONArray(TestDataString.getTestData());
					} else {
						queueArray = APIRetriever.getJSONArray(TBA_ENDPOINT + "event/" + messageEventWrapper.tokens.get(2) + "/matches");
					}
					if( queueArray != null ) {
						EmbedObject[] pages = TBABuilder.paginate(queueArray);
						if( pages != null ) {
							tbaReaction.add(new InteractiveCreateReaction(messageEventWrapper.getChannel(), new PagedInteractive(pages)));
						} else {
							tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "No matches found!")));
						}
					} else {
						tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "You must specify a valid event key!")));
					}
				} else {
					tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify an event key!")));
				}
			} else if( messageEventWrapper.tokens.get(1).equalsIgnoreCase("queue") ) {
				if( messageEventWrapper.tokens.size() > 3 ) {
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
				} else {
					tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify an event key and a team number!")));
				}
			} else if( messageEventWrapper.tokens.get(1).equalsIgnoreCase("alert") ) {
				if( messageEventWrapper.tokens.size() > 3 ) {
					if( messageEventWrapper.getAllMentionedUsers().size() > 0 ) {
						JSONArray queueArray = null;
						if( messageEventWrapper.tokens.get(2).equals("test") ) {
							queueArray = new JSONArray(TestDataString.getTestData());
						} else {
							queueArray = APIRetriever.getJSONArray(TBA_ENDPOINT + "event/" + messageEventWrapper.tokens.get(2) + "/matches");
						}
						if( queueArray != null ) {
							TBAMatchObject[] queue = TBAObjectFactory.generateTBAMatchQueue(queueArray, messageEventWrapper.tokens.get(3));
							if( queue != null ) {
								List<TBAMatchObject> futureQueueList = new ArrayList<TBAMatchObject>();
								for( TBAMatchObject match : queue ) {
									if( match.predictedTime*1000 > System.currentTimeMillis() ) {
										futureQueueList.add(match);
									}
								}
								if( futureQueueList.size() > 0 ) {
									TBAMatchObject[] futureQueue = futureQueueList.toArray(new TBAMatchObject[futureQueueList.size()]);
									tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), "Alert Queue Set!"));
									tbaReaction.add(new SetTimedReaction(new TBAMatchAlertReaction(messageEventWrapper.getChannel(), futureQueue, messageEventWrapper.tokens.get(3), messageEventWrapper.getAllMentionedUsers()), (futureQueue[0].predictedTime-ALERT_SECONDS_BEFORE)*1000));
									tbaReaction.add(new SetTimedReaction(new TBAMatchTimeUpdateReaction(messageEventWrapper.tokens.get(2), messageEventWrapper.tokens.get(3)), System.currentTimeMillis()+1000));
								} else {
									tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.EXECUTION, "All matches complete!")));
								}
							} else {
								tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.EXECUTION, "No matches found!")));
							}
						} else {
							tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "You must specify a valid event key!")));
						}
					} else {
						tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must mention the roles / users to alert!")));
					}
				} else {
					tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify an event key and a team number!")));
				}
			} else {
				tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "You must specify a valid command!")));
			}
		} else {
			tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a command!")));
		}
		return tbaReaction;
	}
	
}
