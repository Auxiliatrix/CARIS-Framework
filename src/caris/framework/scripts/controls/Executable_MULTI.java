package caris.framework.scripts.controls;

import caris.framework.basereactions.QueueReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;

public class Executable_MULTI extends Executable {

	public Executable[] body;
	
	public Executable_MULTI(Executable...body) {
		this.body = body;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException {
		QueueReaction execution = new QueueReaction();
		for( Executable executable : body ) {
			execution.add(executable.execute(mew, context));
		}
		return execution;
	}

	
	
}
