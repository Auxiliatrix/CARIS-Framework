package caris.framework.reactions;

import java.util.ArrayList;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

public class ReactionEmbed extends Reaction {

	public ArrayList<EmbedBuilder> embeds;
	public IChannel channel;
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel) {
		this(embed, channel, 2);
	}
	
	public ReactionEmbed(ArrayList<EmbedBuilder> embeds, IChannel channel) {
		this(embeds, channel, 2);
	}
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel, int priority) {
		super(priority);
		this.embeds = new ArrayList<EmbedBuilder>();
		embeds.add(embed);
		this.channel = channel;
	}
	
	public ReactionEmbed(ArrayList<EmbedBuilder> embeds, IChannel channel, int priority) {
		super(priority);
		this.embeds = embeds;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Logger.say(embeds.toString(), channel);
		for( EmbedBuilder embed : embeds ) {
			BotUtils.sendMessage(channel, embed);
		}
	}
}
