package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;

public class MessageReaction extends Reaction {
	
	public IChannel channel;
	public String message;
	public EmbedObject embed;
	
	public MessageReaction( IChannel channel, String message) {
		this(channel, message, 1);
	}

	public MessageReaction( IChannel channel, EmbedObject embed ) {
		this(channel, embed, 1);
	}

	public MessageReaction( IChannel channel, String message, int priority) {
		this(channel, message, null, priority);
	}
	
	public MessageReaction( IChannel channel, EmbedObject embed, int priority ) {
		this(channel, "", embed, priority);
	}
	
	public MessageReaction( IChannel channel, String message, EmbedObject embed, int priority ) {
		super(priority);
		this.message = message;
		this.embed = embed;
		this.channel = channel;
	}
	
	@Override
	public void process() {
		if( message != "" && embed != null ) {
			Logger.say(message, channel);
			Logger.say(embed.toString(), channel);
			channel.sendMessage(message, embed);
		} else if( message == "" && embed == null ) {
			Logger.error("Message sent with no content.");
		} else if( message.isEmpty() ) {
			Logger.say(embed.toString(), channel);
			channel.sendMessage(embed);
		} else {
			Logger.say(message, channel);
			channel.sendMessage(message);
		}
	}
	
}
