package caris.framework.scripts.controls;

import caris.framework.basereactions.NullReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;
import caris.framework.scripts.ScriptCompiler;

public class Executable_IF extends Executable {

	private String condition;
	private Executable body;
	
	public Executable_IF(String condition, Executable body) {
		this.condition = condition;
		this.body = body;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		if( ScriptCompiler.resolveBooleanVariable(mew, context, condition) ) {
			return body.execute(mew, context);
		} else {
			return new NullReaction();
		}
	}
	
	
	
}
