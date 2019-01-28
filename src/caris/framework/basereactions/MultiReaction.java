package caris.framework.basereactions;

import java.util.List;
import java.util.ArrayList;

public class MultiReaction extends Reaction {

	protected List<Reaction> reactions;
	
	public MultiReaction() {
		this(1);
	}
	
	public MultiReaction(int priority) {
		super(priority);
		reactions = new ArrayList<Reaction>();
	}
	
	public MultiReaction(List<Reaction> reactions) {
		this(reactions, 1);
	}
	
	public MultiReaction(List<Reaction> reactions, int priority) {
		super(priority);
		this.reactions = reactions;
	}
	
	public boolean add(Reaction reaction) {
		return reactions.add(reaction);
	}
	
	@Override
	public void run() {
		for( Reaction reaction : reactions ) {
			reaction.run();
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
