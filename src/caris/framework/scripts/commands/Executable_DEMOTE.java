package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.RoleRemoveReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;
import caris.framework.scripts.ScriptCompiler;

public class Executable_DEMOTE extends Executable {

	private String user;
	private String role;
	
	public Executable_DEMOTE(String user, String role) {
		this.user = user;
		this.role = role;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		return new RoleRemoveReaction(ScriptCompiler.resolveUserVariable(mew, context, user), ScriptCompiler.resolveRoleVariable(mew, context, role));
	}

}
