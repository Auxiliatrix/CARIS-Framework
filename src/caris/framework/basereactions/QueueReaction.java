package caris.framework.basereactions;

import java.util.ArrayList;
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
		for( Reaction reaction : unpackReactions() ) {
			try {
				reaction.run(); // Intentional; these reactions should be executed sequentially.
			} catch ( Exception e ){
				e.printStackTrace();
			}
		}
	}
	
	// Necessary to put all reactions into one list to avoid linear buffer interfering with recursive execution
	public List<Reaction> unpackReactions() {
		List<Reaction> unpacked = new ArrayList<Reaction>();
		for( Reaction reaction : reactions ) {
			if( reaction instanceof QueueReaction ) {
				for( Reaction subReaction : ((QueueReaction) reaction).unpackReactions() ) {
					unpacked.add(subReaction);
				}
			} else {
				unpacked.add(reaction);
			}
		}
		return unpacked;
	}
	
}
