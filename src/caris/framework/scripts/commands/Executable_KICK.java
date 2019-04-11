package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.KickReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;

public class Executable_KICK extends Executable {

	private String user;
	
	public Executable_KICK(String user) {
		this.user = user;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		return new KickReaction(mew.getGuild(), ScriptCompiler.compileUserVariable(mew, context, user));
	}

}
