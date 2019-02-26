package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.tokens.Duration;

public class SetTimedReaction extends Reaction {

	public Reaction event;
	public long target;
	
	public SetTimedReaction(Reaction event, Duration timer, long timeStamp) {
		this(event, timer, timeStamp, 1);
	}
	
	public SetTimedReaction(Reaction event, long target) {
		this(event, target, 1);
	}
	
	public SetTimedReaction(Reaction event, Duration timer, long timeStamp, int priority) {
		this(event, timeStamp + timer.asMili());
	}
	
	public SetTimedReaction(Reaction event, long target, int priority) {
		super(priority);
		this.event = event;
		this.target = target;
	}
	
	@Override
	public void process() {
		Brain.timedQueue.put(event, target);
	}
	
}
