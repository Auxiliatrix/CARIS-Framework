package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class MessageDeleteReaction extends Reaction {

	public IChannel channel;
	private IMessage message;
	
	public MessageDeleteReaction( IChannel channel, IMessage message ) {
		this(channel, message, 2);
	}
	
	public MessageDeleteReaction( IChannel channel, IMessage message, int priority) {
		super(priority);
		this.channel = channel;
		this.message = message;
	}
	
	@Override
	public void process() {
		if( message != null ) {
			Long id = message.getLongID();
			message.delete();
			Logger.print("Message (" + id + ") deleted", 2);
		}
	}
	
}
