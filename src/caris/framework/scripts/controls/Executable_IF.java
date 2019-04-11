package caris.framework.scripts.controls;

import caris.framework.basereactions.NullReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Conditional;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;

public class Executable_IF extends Executable {

	private Conditional condition;
	private Executable body;
	
	public Executable_IF(Conditional condition, Executable body) {
		this.condition = condition;
		this.body = body;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		if( condition.resolve(mew, context) ) {
			return body.execute(mew, context);
		} else {
			return new NullReaction();
		}
	}
	
	
	
}
