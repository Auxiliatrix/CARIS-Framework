package caris.framework.reactions;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.tokens.Duration;

public class SetTimedReaction extends Reaction {

	public Reaction event;
	public Duration timer;
	public long timeStamp;
	
	public SetTimedReaction(Reaction event, Duration timer, long timeStamp) {
		this(event, timer, timeStamp, 1);
	}
	
	public SetTimedReaction(Reaction event, Duration timer, long timeStamp, int priority) {
		super(priority);
		this.event = event;
		this.timer = timer;
		this.timeStamp = timeStamp;
	}
	
	@Override
	public void process() {
		long target = timeStamp * 1000 + timer.asMili();
		if( Brain.timedQueue.containsKey(target) ) {
			Brain.timedQueue.get(target).add(event);
		} else {
			List<Reaction> events = new ArrayList<Reaction>();
			events.add(event);
			Brain.timedQueue.put(target, events);
		}
	}
	
}
