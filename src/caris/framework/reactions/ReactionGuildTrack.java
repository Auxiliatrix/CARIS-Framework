package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.GuildInfo;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IGuild;

public class ReactionGuildTrack extends Reaction {

	public IGuild guild;
	
	public ReactionGuildTrack(IGuild guild) {
		this(guild, -1);
	}
	
	public ReactionGuildTrack(IGuild guild, int priority) {
		super(-1);
		this.guild = guild;
	}
	
	@Override
	public void run() {
		if( !Variables.guildIndex.containsKey(guild) ) {
			GuildInfo guildInfo = new GuildInfo( guild.getName(), guild );
			Variables.guildIndex.put( guild, guildInfo );
		}
		Logger.print("Guild (" + guild.getLongID() + ") <" + guild.getName() + "> loaded");
	}
	
}
