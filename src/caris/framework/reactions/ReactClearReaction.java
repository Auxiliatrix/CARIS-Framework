package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IMessage;

public class ReactClearReaction extends Reaction {

	public IMessage message;
	
	public ReactClearReaction(IMessage message) {
		this(message, 1);
	}
	
	public ReactClearReaction(IMessage message, int priority) {
		super(priority);
		this.message = message;
	}
	
	@Override
	public void process() {
		message.removeAllReactions();
	}
	
}
