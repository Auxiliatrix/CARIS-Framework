package caris.framework.reactions;

public abstract class Reaction extends Thread {

	public enum Tag {
		DEFAULT,
		PASSIVE,
		DOMINANT,
		RECESSIVE
	}
	
	protected Tag tag;
	
	public Reaction() {
		this(Tag.DEFAULT);
	}
	
	public Reaction(Tag tag) {
		this.tag = tag;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	protected abstract void task();
	
	@Override
	public final void run() {
		task();
	}

}
