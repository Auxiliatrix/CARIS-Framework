package caris.framework.basereactions;

import java.util.List;

public class QueueReaction extends MultiReaction {

	public QueueReaction() {
		super();
	}
	
	public QueueReaction(int priority) {
		super(priority);
	}
	
	public QueueReaction(List<Reaction> reactions) {
		super(reactions);
	}
	
	public QueueReaction(List<Reaction> reactions, int priority) {
		super(reactions, priority);
	}
	
	@Override
	public void process() {
		for( Reaction reaction : reactions ) {
			try {
				reaction.run(); // Intentional; these reactions should be executed sequentially.
			} catch ( Exception e ){
				e.printStackTrace();
			}
		}
	}
	
}
