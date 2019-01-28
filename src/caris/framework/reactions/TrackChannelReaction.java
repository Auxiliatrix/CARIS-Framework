package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class TrackChannelReaction extends Reaction {
		
	public IGuild guild;
	public IChannel channel;
	
	public TrackChannelReaction(IGuild guild, IChannel channel) {
		this(guild, channel, -1);
	}
	
	public TrackChannelReaction(IGuild guild, IChannel channel, int priority) {
		super(priority);
		this.guild = guild;
		this.channel = channel;
	}
	
	@Override
	public void process() {
		Brain.variables.guildIndex.get(guild.getLongID()).addChannel(channel);
		Logger.print("Channel <" + channel.getName() + "> (" + channel.getLongID() + ") added to Guild <" + guild.getName() + "> (" + guild.getLongID() + ")", 0);
	}
	
}
