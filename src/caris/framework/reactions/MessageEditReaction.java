package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.tokens.MessageContent;
import sx.blah.discord.handle.obj.IMessage;

public class MessageEditReaction extends Reaction {

	public IMessage message;
	public MessageContent content;
	
	public MessageEditReaction(IMessage message, MessageContent content) {
		this(message, content, -1);
	}
	
	public MessageEditReaction(IMessage message, MessageContent content, int priority) {
		super(priority);
		this.message = message;
		this.content = content;
	}
	
	@Override
	public void process() {
		if( content.content != "" && content.embed != null ) {
			message.edit(content.content, content.embed);
		} else if( content.content == "" && content.embed == null ) {
			message.edit("```http\nLoading Interactive...\n```");
		} else if( content.content.isEmpty() ) {
			message.edit(content.embed);
		} else {
			message.edit(content.content);
		}
	}

}
