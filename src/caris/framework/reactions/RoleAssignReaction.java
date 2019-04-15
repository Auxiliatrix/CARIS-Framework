package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class RoleAssignReaction extends Reaction {

	public IUser user;
	public IRole role;
	
	public RoleAssignReaction( IUser user, IRole role ) {
		this(user, role, 1);
	}
	
	public RoleAssignReaction( IUser user, IRole role, int priority ) {
		super(priority);
		this.user = user;
		this.role = role;
	}
	
	public void process() {
		user.addRole(role);
		Logger.print("Role \"" + role.getName() + "\" added to " + user.getName(), 1);
	}
	
}
