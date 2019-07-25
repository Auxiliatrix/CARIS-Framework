package caris.framework.reactions;

import java.util.ArrayList;
import java.util.List;

public class QueueReaction extends Reaction {
	
	private List<Reaction> reactions;
	
	public QueueReaction( List<Reaction> reactions ) {
		super();
		this.reactions = reactions;
	}
	
	public QueueReaction( List<Reaction> reactions, Tag tag ) {
		super(tag);
		this.reactions = reactions;
	}

	@Override
	protected void task() {
		for( Reaction reaction : unpackedReactions() ) {
			reaction.run();
		}
	}
	
	public List<Reaction> unpackedReactions() {
		List<Reaction> unpacked = new ArrayList<Reaction>();
		for( Reaction reaction : reactions ) {
			if( reaction instanceof QueueReaction ) {
				unpacked.addAll(((QueueReaction) reaction).unpackedReactions());
			} else {
				unpacked.add(reaction);
			}
		}
		return unpacked;
	}
	
}
