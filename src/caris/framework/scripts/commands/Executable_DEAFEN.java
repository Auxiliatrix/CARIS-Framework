package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.DeafenReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;

public class Executable_DEAFEN extends Executable {

	private String user;
	
	public Executable_DEAFEN(String user) {
		this.user = user;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		return new DeafenReaction(mew.getGuild(), ScriptCompiler.compileUserVariable(mew, context, user), true);
	}

}
