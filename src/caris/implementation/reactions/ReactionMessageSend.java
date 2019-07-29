package caris.implementation.reactions;

import caris.framework.reactions.Reaction;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionMessageSend extends Reaction {
	
	private IChannel channel;
	private String message;
	private EmbedObject embed;
	
	public ReactionMessageSend(IChannel channel, String message) {
		this(Tag.DEFAULT, channel, message, null);
	}
	
	public ReactionMessageSend(Tag tag, IChannel channel, String message) {
		this(tag, channel, message, null);
	}
	
	public ReactionMessageSend(IChannel channel, EmbedObject embed) {
		this(Tag.DEFAULT, channel, "", embed);
	}
	
	public ReactionMessageSend(Tag tag, IChannel channel, EmbedObject embed) {
		this(tag, channel, "", embed);
	}
	
	public ReactionMessageSend(IChannel channel, String message, EmbedObject embed) {
		this(Tag.DEFAULT, channel, message, embed);
	}

	public ReactionMessageSend(Tag tag, IChannel channel, String message, EmbedObject embed) {
		super(tag);
		this.channel = channel;
		this.message = message;
		this.embed = embed;
	}
	
	@Override
	protected void task() {
		if( embed != null && !message.isEmpty() && message != null ) {
			channel.sendMessage(message, embed);
		} else if( embed != null ) {
			channel.sendMessage(embed);
		} else if( !message.isEmpty() && message != null ) {
			channel.sendMessage(message);
		}
	}
	
}
