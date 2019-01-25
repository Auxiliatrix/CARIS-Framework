package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;

public class ReactRemoveReaction extends Reaction {

	public IMessage message;
	public IUser user;
	public IReaction reaction;
	
	public ReactRemoveReaction(IMessage message, IUser user, IReaction reaction) {
		this(message, user, reaction, 1);
	}
	
	public ReactRemoveReaction(IMessage message, IUser user, IReaction reaction, int priority) {
		super(priority);
		this.message = message;
		this.user = user;
		this.reaction = reaction;
	}

	@Override
	public void run() {
		if( reaction.getUserReacted(user) ) {
			message.removeReaction(user, reaction);
		}
	}
	
}
