package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class TrackUserReaction extends Reaction {

	public IGuild guild;
	public IUser user;
	
	public TrackUserReaction(IGuild guild, IUser user) {
		this(guild, user, -1);
	}
	
	public TrackUserReaction(IGuild guild, IUser user, int priority) {
		super(priority);
		this.guild = guild;
		this.user = user;
	}
	
	@Override
	public void run() {
		Brain.variables.guildIndex.get(guild.getLongID()).addUser(user);
		Logger.print("User <" + user.getName() + "#" + user.getDiscriminator() + "> (" + user.getLongID() + ") joined Guild <" + guild.getName() + "> (" + guild.getLongID() + ")", 0);
	}
	
}
