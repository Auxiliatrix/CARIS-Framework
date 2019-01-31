package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;

public class ReconnectReaction extends Reaction {

	public ReconnectReaction() {
		this(-1);
	}
	
	public ReconnectReaction(int priority) {
		super(-1);
	}
	
	@Override
	public void process() {
		if( !Brain.cli.isReady() || !Brain.cli.isLoggedIn() ) {
			Brain.cli.login();
		}
	}
	
}
