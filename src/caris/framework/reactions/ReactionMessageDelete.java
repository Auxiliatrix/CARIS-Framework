package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class ReactionMessageDelete extends Reaction {

	public IChannel channel;
	private IMessage message;
	
	public ReactionMessageDelete( IChannel channel, IMessage message ) {
		this(channel, message, 2);
	}
	
	public ReactionMessageDelete( IChannel channel, IMessage message, int priority) {
		super(priority);
		this.channel = channel;
		this.message = message;
	}
	
	@Override
	public void run() {
		if( message != null ) {
			Long id = message.getLongID();
			message.delete();
			Logger.print("Message (" + id + ") deleted", 2);
		}
	}
	
}
