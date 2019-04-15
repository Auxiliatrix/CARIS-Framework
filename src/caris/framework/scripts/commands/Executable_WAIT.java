package caris.framework.scripts.commands;

import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.WaitReaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;

public class Executable_WAIT extends Executable {

	private String time;
	
	public Executable_WAIT(String time) {
		this.time = time;
	}

	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		return new WaitReaction(ScriptCompiler.resolveNumberVariable(mew, context, time));
	}
	
}
