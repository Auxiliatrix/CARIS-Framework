package caris.framework.scripts.comparators;

import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Conditional;
import caris.framework.scripts.Context;

public class Conditional_NOT extends Conditional {

	private Conditional condition;
	
	public Conditional_NOT(Conditional condition) {
		this.condition = condition;
	}

	@Override
	public boolean resolve(MessageEventWrapper mew, Context context) {
		return !condition.resolve(mew, context);
	}
	
}
