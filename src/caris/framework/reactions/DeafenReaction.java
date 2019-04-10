package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class DeafenReaction extends Reaction {

	private IGuild guild;
	private IUser user;
	private boolean state;
	
	public DeafenReaction(IGuild guild, IUser user, boolean state) {
		this.guild = guild;
		this.user = user;
	}
	
	@Override
	public void process() {
		guild.setDeafenUser(user, state);
	}

}
