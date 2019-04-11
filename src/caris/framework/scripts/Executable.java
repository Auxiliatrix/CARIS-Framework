package caris.framework.scripts;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;

public abstract class Executable {
	
	public abstract Reaction execute(MessageEventWrapper mew, Context context);
	
}
