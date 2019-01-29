package caris.framework.reactions;

import caris.framework.basehandlers.InteractiveHandler;
import caris.framework.basereactions.Reaction;

public class InteractiveDestroyReaction extends Reaction {

	public InteractiveHandler interactive;
	
	public InteractiveDestroyReaction(InteractiveHandler interactive) {
		this(interactive, -1);
	}
	
	public InteractiveDestroyReaction(InteractiveHandler interactive, int priority) {
		super(priority);
		this.interactive = interactive;
	}
	
	@Override
	public void process() {
		interactive.destroy().start();
	}
	
}
