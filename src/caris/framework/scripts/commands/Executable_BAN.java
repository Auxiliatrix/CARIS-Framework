package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.BanReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;

public class Executable_BAN extends Executable {

	private String user;
	
	public Executable_BAN(String user) {
		this.user = user;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		return new BanReaction(mew.getGuild(), ScriptCompiler.resolveUserVariable(mew, context, user));
	}

}
