package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MuteReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;

public class Executable_UNMUTE extends Executable {

	private String user;
	
	public Executable_UNMUTE(String user) {
		this.user = user;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		return new MuteReaction(mew.getGuild(), ScriptCompiler.resolveUserVariable(mew, context, user), false);
	}

}
