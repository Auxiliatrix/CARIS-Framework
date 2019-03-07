package caris.modular.handlers;

import java.util.List;

import com.mashape.unirest.http.Unirest;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.events.MessageEventWrapper;
import sx.blah.discord.handle.obj.Permissions;

public class FeedHandler extends MessageHandler {

	public FeedHandler() {
		super("Feed", "frc", Permissions.MANAGE_CHANNELS);
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
		MultiReaction feedReaction = new MultiReaction(0);
		
		return feedReaction;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
