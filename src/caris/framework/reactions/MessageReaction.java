package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;

public class MessageReaction extends Reaction {
	
	public String message;
	public IChannel channel;
	
	public MessageReaction( String message, IChannel channel ) {
		this(message, channel, 1);
	}
	
	public MessageReaction( String message, IChannel channel, int priority ) {
		super(priority);
		this.message = message;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Logger.say(message, channel);
		BotUtils.sendMessage(channel, message);
	}
	
}
