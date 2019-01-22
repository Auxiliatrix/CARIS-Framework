package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import sx.blah.discord.handle.obj.IUser;

public class ReactionGlobalUserUpdate extends Reaction {

	public IUser user;
	public String key;
	public Object value;
	public boolean override;
	
	public ReactionGlobalUserUpdate(IUser user, String field, Object value) {
		this(user, field, value, false, -1);
	}
	
	public ReactionGlobalUserUpdate(IUser user, String field, Object value, boolean override) {
		this(user, field, value, override, -1);
	}
	
	public ReactionGlobalUserUpdate(IUser user, String field, Object value, int priority) {
		this(user, field, value, false, priority);
	}
	
	public ReactionGlobalUserUpdate(IUser user, String field, Object value, boolean override, int priority) {
		super(priority);
	}
	
	@Override
	public void run() {
		if( override || !Variables.globalUserInfo.get(user).userData.containsKey(key) ) {
			Variables.globalUserInfo.get(user).userData.put(key, value);
		}
	}

}
