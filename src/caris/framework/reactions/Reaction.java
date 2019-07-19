package caris.framework.reactions;

public class Reaction implements Runnable {

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
	
	@Override
	public void run() {
		
	}

}
