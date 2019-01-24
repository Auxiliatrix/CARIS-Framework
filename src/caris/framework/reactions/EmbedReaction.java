package caris.framework.reactions;

import java.util.ArrayList;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;

public class EmbedReaction extends Reaction {

	public ArrayList<EmbedObject> embeds;
	public IChannel channel;
	
	public EmbedReaction(EmbedObject embed, IChannel channel) {
		this(embed, channel, 2);
	}
	
	public EmbedReaction(ArrayList<EmbedObject> embeds, IChannel channel) {
		this(embeds, channel, 2);
	}
	
	public EmbedReaction(EmbedObject embed, IChannel channel, int priority) {
		super(priority);
		this.embeds = new ArrayList<EmbedObject>();
		embeds.add(embed);
		this.channel = channel;
	}
	
	public EmbedReaction(ArrayList<EmbedObject> embeds, IChannel channel, int priority) {
		super(priority);
		this.embeds = embeds;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Logger.say(embeds.toString(), channel);
		for( EmbedObject embed : embeds ) {
			BotUtils.sendMessage(channel, embed);
		}
	}
}
