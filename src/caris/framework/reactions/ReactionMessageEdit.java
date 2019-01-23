package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IMessage;

public class ReactionMessageEdit extends Reaction {

	public IMessage message;
	public String content;
	
	public ReactionMessageEdit(IMessage message, String content) {
		this(message, content, -1);
	}
	
	public ReactionMessageEdit(IMessage message, String content, int priority) {
		super(priority);
		this.message = message;
		this.content = content;
	}
	
	@Override
	public void run() {
		message.edit(content);
	}

}
