package caris.framework.scripts.comparators;

import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Conditional;
import caris.framework.scripts.Context;

public class Conditional_AND extends Conditional {

	private Conditional condition1;
	private Conditional condition2;
	
	public Conditional_AND(Conditional condition1, Conditional condition2) {
		this.condition1 = condition1;
		this.condition2 = condition2;
	}

	@Override
	public boolean resolve(MessageEventWrapper mew, Context context) {
		return condition1.resolve(mew, context) && condition2.resolve(mew, context);
	}
	
}
