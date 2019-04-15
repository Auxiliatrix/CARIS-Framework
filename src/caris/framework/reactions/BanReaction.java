package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class BanReaction extends Reaction {

	private IGuild guild;
	private IUser user;
	private String reason;
	
	public BanReaction(IGuild guild, IUser user) {
		this(guild, user, "");
	}
	
	public BanReaction(IGuild guild, IUser user, String reason) {
		this.guild = guild;
		this.user = user;
		this.reason = reason;
	}

	@Override
	public void process() {
		if( reason.isEmpty() ) {
			guild.banUser(user);			
		} else {
			guild.banUser(user, reason);
		}
	}
	
}
