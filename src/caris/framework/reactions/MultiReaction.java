package caris.framework.reactions;

import java.util.List;

public class MultiReaction extends Reaction {

	private List<Reaction> reactions;
	
	public MultiReaction( List<Reaction> reactions ) {
		super();
		this.reactions = reactions;
	}
	
	public MultiReaction( List<Reaction> reactions, Tag tag ) {
		super(tag);
		this.reactions = reactions;
	}

	@Override
	protected void task() {
		for( Reaction reaction : reactions ) {
			reaction.start();
		}
	}
	
}
