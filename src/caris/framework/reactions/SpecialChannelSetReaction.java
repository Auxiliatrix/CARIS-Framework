package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.GuildInfo.SpecialChannel;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class SpecialChannelSetReaction extends Reaction {

	public IGuild guild;
	public SpecialChannel sc;
	public IChannel channel;
	
	public SpecialChannelSetReaction(IGuild guild, SpecialChannel sc, IChannel channel) {
		this(guild, sc, channel, 1);
	}
	
	public SpecialChannelSetReaction(IGuild guild, SpecialChannel sc, IChannel channel, int priority) {
		super(priority);
		this.guild = guild;
		this.sc = sc;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		if( sc == null ) {
			Brain.variables.guildIndex.get(guild.getLongID()).specialChannels.remove(sc);
		}
		Brain.variables.guildIndex.get(guild.getLongID()).specialChannels.put(sc, channel.getLongID());
	}
}
