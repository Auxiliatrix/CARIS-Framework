package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class HearReaction extends Reaction {

	public String message;
	public IUser user;
	public IChannel channel;
	
	public HearReaction(String message, IUser user, IChannel channel) {
		this(message, user, channel, -1);
	}
	
	public HearReaction(String message, IUser user, IChannel channel, int priority) {
		super(priority);
		this.message = message;
		this.user = user;
		this.channel = channel;
		this.priority = priority;
	}
	
	@Override
	public void process() {
		try {
			Logger.hear(message, user, channel);
		} catch(NullPointerException n) {
			Logger.error("Fatal error: null pointer in reactionhear");
		}
	}
	
}
