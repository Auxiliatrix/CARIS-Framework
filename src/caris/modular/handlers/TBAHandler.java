package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.mashape.unirest.http.Unirest;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.interactives.PagedInteractive;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import caris.modular.embedbuilders.TBABuilder;
import caris.modular.utilities.APIRetriever;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

public class TBAHandler extends MessageHandler {

	public static final String TBA_ENDPOINT = "https://www.thebluealliance.com/api/v3/";
	
	public TBAHandler() {
		super("TBA", false);
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
			if( messageEventWrapper.tokens.get(1).toLowerCase().equals("queue") ) {
				if( messageEventWrapper.tokens.size() > 2 ) {
					JSONArray queueArray = APIRetriever.getJSONArray(TBA_ENDPOINT + "event/" + messageEventWrapper.tokens.get(2) + "/matches");
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
			} else {
				tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.KEYWORD, "You must specify a valid command!")));
			}
		} else {
			tbaReaction.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a command!")));
		}
		return tbaReaction;
	}

	@Override
	public String getDescription() {
		return "Reads from thebluealliance website to provide live tournament data.";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add(invocation + " queue <event_key>");
		return usage;
	}
	
}
