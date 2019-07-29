package caris.implementation.reactions;

import caris.framework.reactions.Reaction;

public class WaitReaction extends Reaction {

	private int millis;
	
	public WaitReaction(int millis) {
		super();
		this.millis = millis;
	}
	
	public WaitReaction(int millis, Tag tag) {
		super(tag);
		this.millis = millis;
	}
	
	@Override
	protected void task() {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
