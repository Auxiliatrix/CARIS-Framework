package caris.framework.scripts;

import caris.framework.events.MessageEventWrapper;

public abstract class Conditional {

	public abstract boolean resolve(MessageEventWrapper mew, Context context);
	
}
