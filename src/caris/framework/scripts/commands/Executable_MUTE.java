package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MuteReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import caris.framework.scripts.Executable;

public class Executable_MUTE extends Executable {

	private String user;
	private boolean override;
	
	public Executable_MUTE(String user, boolean override) {
		this.user = user;
		this.override = override;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		IUser target = ScriptCompiler.resolveUserVariable(mew, context, user);
		breakIfIllegal(mew.getGuild(), mew.getAuthor(), target, override, Permissions.VOICE_MUTE_MEMBERS, "mute");
		return new MuteReaction(mew.getGuild(), target, true);
	}

}
