package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ReactionChannelTrack extends Reaction {
		
	public IGuild guild;
	public IChannel channel;
	
	public ReactionChannelTrack(IGuild guild, IChannel channel) {
		this(guild, channel, -1);
	}
	
	public ReactionChannelTrack(IGuild guild, IChannel channel, int priority) {
		super(priority);
		this.guild = guild;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Variables.guildIndex.get(guild).addChannel(channel);
		Logger.print("Channel <" + channel.getName() + "> (" + channel.getLongID() + ") added to Guild <" + guild.getName() + "> (" + guild.getLongID() + ")", 0);
	}
	
}
