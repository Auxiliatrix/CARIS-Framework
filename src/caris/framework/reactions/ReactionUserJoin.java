package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ReactionUserJoin extends Reaction {

	public IGuild guild;
	public IUser user;
	
	public ReactionUserJoin(IGuild guild, IUser user) {
		this(guild, user, -1);
	}
	
	public ReactionUserJoin(IGuild guild, IUser user, int priority) {
		super(priority);
		this.guild = guild;
		this.user = user;
	}
	
	@Override
	public void run() {
		Variables.guildIndex.get(guild).addUser(user);
		Logger.print("User <" + user.getName() + "#" + user.getDiscriminator() + "> (" + user.getLongID() + ") joined Guild <" + guild.getName() + "> (" + guild.getLongID() + ")", 0);
	}
	
}
