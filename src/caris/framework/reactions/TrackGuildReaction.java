package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IGuild;

public class TrackGuildReaction extends Reaction {

	public IGuild guild;
	
	public TrackGuildReaction(IGuild guild) {
		this(guild, -1);
	}
	
	public TrackGuildReaction(IGuild guild, int priority) {
		super(-1);
		this.guild = guild;
	}
	
	@Override
	public void process() {
		Brain.variables.addGuild(guild);
		Logger.print("Guild (" + guild.getLongID() + ") <" + guild.getName() + "> loaded");
	}
	
}
