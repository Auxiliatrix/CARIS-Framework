package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.DeafenReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import caris.framework.scripts.Executable;

public class Executable_UNDEAFEN extends Executable {

	private String user;
	private boolean override;
	
	public Executable_UNDEAFEN(String user, boolean override) {
		this.user = user;
		this.override = override;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		IUser target = ScriptCompiler.resolveUserVariable(mew, context, user);
		breakIfIllegal(mew.getGuild(), mew.getAuthor(), target, override, Permissions.VOICE_DEAFEN_MEMBERS, "undeafen");
		return new DeafenReaction(mew.getGuild(), target, false);
	}

}
