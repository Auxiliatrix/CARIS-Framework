package caris.framework.basereactions;

public class WaitReaction extends Reaction {

	private int millis;
	
	public WaitReaction(int millis) {
		this.millis = millis;
	}
	
	@Override
	public void process() {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
