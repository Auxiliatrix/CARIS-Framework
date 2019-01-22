package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class ReactionMessageLog extends Reaction {
	
	public IChannel channel;
	public IMessage message;
	
	public ReactionMessageLog(IChannel channel, IMessage message) {
		this(channel, message, -1);
	}
	
	public ReactionMessageLog(IChannel channel, IMessage message, int priority) {
		super(priority);
		this.channel = channel;
		this.message = message;
	}
	
	@Override
	public void run() {
		if(!channel.isPrivate()) {
		Logger.print("Logged message from (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
		}
	}
}
