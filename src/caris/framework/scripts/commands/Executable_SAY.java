package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;

public class Executable_SAY extends Executable {

	private String message;
	
	public Executable_SAY(String message) {
		this.message = message;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		return new MessageReaction(mew.getChannel(), ScriptCompiler.compileFormattedString(mew, context, message));
	}

	
	
}
