package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.RoleRemoveReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;
import caris.framework.scripts.ScriptCompiler;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Executable_DEMOTE extends Executable {

	private String user;
	private String role;
	private boolean override;
	
	public Executable_DEMOTE(String user, String role, boolean override) {
		this.user = user;
		this.role = role;
		this.override = override;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		IUser target = ScriptCompiler.resolveUserVariable(mew, context, user);
		IRole demotion = ScriptCompiler.resolveRoleVariable(mew, context, role);
		breakIfIllegal(mew.getGuild(), mew.getAuthor(), target, override, demotion, "demote");
		return new RoleRemoveReaction(target, demotion);
	}

}
