package caris.framework.scripts.controls;

import caris.framework.basereactions.QueueReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;

public class Executable_FOR extends Executable {
	
	private String counter;
	private String range;
	private Executable body;
	
	public Executable_FOR(String counter, String range, Executable body) {
		this.counter = counter;
		this.range = range;
		this.body = body;
	}

	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		QueueReaction execution = new QueueReaction();
		for( int f=0; f<ScriptCompiler.resolveIntVariable(mew, context, range); f++ ) {
			Context newContext = new Context(context);
			newContext.putInt(counter, f+1);
			Reaction reaction = body.execute(mew, newContext);
			if( reaction == null ) {
				break;
			}
			execution.add(reaction);
		}
		return execution;
	}
	
}
