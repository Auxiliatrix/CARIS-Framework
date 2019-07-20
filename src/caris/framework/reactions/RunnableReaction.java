package caris.framework.reactions;

public class RunnableReaction extends Reaction {

	private Runnable runnable;
	
	public RunnableReaction(Runnable runnable) {
		super();
		this.runnable = runnable;
	}
	
	public RunnableReaction(Runnable runnable, Tag tag) {
		super(tag);
		this.runnable = runnable;
	}

	@Override
	protected void task() {
		runnable.run();
	}
	
}
