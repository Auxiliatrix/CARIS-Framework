package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.RoleAssignReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;
import caris.framework.scripts.ScriptCompiler;

public class Executable_PROMOTE extends Executable {

	private String user;
	private String role;
	
	public Executable_PROMOTE(String user, String role) {
		this.user = user;
		this.role = role;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		return new RoleAssignReaction(ScriptCompiler.resolveUserVariable(mew, context, user), ScriptCompiler.resolveRoleVariable(mew, context, role));
	}

}
