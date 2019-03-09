package caris.framework.reactions;

import caris.framework.basehandlers.InteractiveModule;
import caris.framework.basereactions.Reaction;

public class InteractiveDestroyReaction extends Reaction {

	public InteractiveModule interactive;
	
	public InteractiveDestroyReaction(InteractiveModule interactive) {
		this(interactive, -1);
	}
	
	public InteractiveDestroyReaction(InteractiveModule interactive, int priority) {
		super(priority);
		this.interactive = interactive;
	}
	
	@Override
	public void process() {
		if( !interactive.completed ) {
			interactive.destroy().start();
		}
	}
	
}
