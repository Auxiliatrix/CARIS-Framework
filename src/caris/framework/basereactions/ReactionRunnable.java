package caris.framework.basereactions;

public class ReactionRunnable extends Reaction {

	public Runnable runnable;
	
	public ReactionRunnable(Runnable runnable) {
		this(runnable, 0);
	}
	
	public ReactionRunnable(Runnable runnable, int priority) {
		super(priority);
		this.runnable = runnable;
	}
	
	@Override
	public void run() {
		runnable.run();
	}
	
}
