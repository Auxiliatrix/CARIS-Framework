package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import sx.blah.discord.handle.obj.IUser;

public class BalanceReaction extends Reaction {

	public IUser user;
	public int balance;
	
	public BalanceReaction(IUser user, int balance) {
		this(user, balance, 1);
	}
	
	public BalanceReaction(IUser user, int balance, int priority) {
		super(priority);
		this.user = user;
		this.balance = balance;
	}
	
	@Override
	public void run() {
		Variables.globalUserInfo.get(user).balance += balance;
	}

}
