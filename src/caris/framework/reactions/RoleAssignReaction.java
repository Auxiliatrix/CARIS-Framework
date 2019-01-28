package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IUser;

public class RoleAssignReaction extends Reaction {

	public IUser user;
	public Role role;
	
	public RoleAssignReaction( IUser user, Role role ) {
		this(user, role, 1);
	}
	
	public RoleAssignReaction( IUser user, Role role, int priority ) {
		super(priority);
		this.user = user;
		this.role = role;
	}
	
	public void process() {
		user.addRole(role);
		Logger.print("Role \"" + role.getName() + "\" added to " + user.getName(), 1);
	}
}
