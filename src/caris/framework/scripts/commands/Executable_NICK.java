package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.NicknameSetReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;
import caris.framework.scripts.ScriptCompiler;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class Executable_NICK extends Executable {

	private String user;
	private String name;
	private boolean override;
	
	public Executable_NICK(String user, String name, boolean override) {
		this.user = user;
		this.name = name;
		this.override = override;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		IUser target = ScriptCompiler.resolveUserVariable(mew, context, user);
		breakIfIllegal(mew.getGuild(), mew.getAuthor(), target, override, Permissions.MANAGE_NICKNAMES, "nickname");
		return new NicknameSetReaction(mew.getGuild(), target, name);
	}

}
