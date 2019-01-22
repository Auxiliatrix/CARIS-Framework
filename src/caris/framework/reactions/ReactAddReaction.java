package caris.framework.reactions;

import com.vdurmont.emoji.Emoji;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IMessage;

public class ReactAddReaction extends Reaction {
	
	public IMessage message;
	public Emoji emoji;
	
	public ReactAddReaction(IMessage message, Emoji emoji) {
		this(message, emoji, -1);
	}
	
	public ReactAddReaction(IMessage message, Emoji emoji, int priority) {
		super(priority);
		this.message = message;
		this.emoji = emoji;
	}
	
	@Override
	public void run() {
		message.addReaction(emoji);
		Logger.print("Reaction [" + emoji.getAliases().toString() + "] added to message (" + message.getLongID() + ")", 3);
	}
	
}
