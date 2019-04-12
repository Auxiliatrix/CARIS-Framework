package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class RoleRemoveReaction extends Reaction {

	public IUser user;
	public IRole role;
	
	public RoleRemoveReaction( IUser user, IRole role ) {
		this(user, role, 1);
	}
	
	public RoleRemoveReaction( IUser user, IRole role, int priority ) {
		super(priority);
		this.user = user;
		this.role = role;
	}
	
	public void process() {
		if( user.hasRole(role) ) {
			user.removeRole(role);
			Logger.print("Role \"" + role.getName() + "\" removed from " + user.getName(), 1);
		}
	}

}
