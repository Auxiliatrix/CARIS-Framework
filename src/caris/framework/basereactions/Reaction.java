package caris.framework.basereactions;

import caris.framework.main.Brain;

public abstract class Reaction extends Thread implements Comparable<Reaction> {
	
	public int priority;
	
	public Reaction() {
		this(0);
	}
	
	public Reaction(int priority) {
		this.priority = priority;
	}
	
	@Override
	public void run() {
		Brain.threadCount.incrementAndGet();
		try {
			process();
			Brain.threadCount.decrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
			Brain.threadCount.decrementAndGet();
		}
	}
	
	public abstract void process();
	
	@Override
	public int compareTo(Reaction r) {
		int compare = r.priority;
		if( this.priority < compare ) {
			return 1;
		} else if( this.priority > compare ) {
			return -1;
		} else {
			return 0;
		}
	}
}
