package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IUser;

public class UpdateGlobalUserReaction extends Reaction {

	public IUser user;
	public String key;
	public Object value;
	public boolean override;
	
	public UpdateGlobalUserReaction(IUser user, String key, Object value) {
		this(user, key, value, false, -1);
	}
	
	public UpdateGlobalUserReaction(IUser user, String key, Object value, boolean override) {
		this(user, key, value, override, -1);
	}
	
	public UpdateGlobalUserReaction(IUser user, String key, Object value, int priority) {
		this(user, key, value, false, priority);
	}
	
	public UpdateGlobalUserReaction(IUser user, String key, Object value, boolean override, int priority) {
		super(priority);
		this.user = user;
		this.key = key;
		this.value = value;
		this.override = override;
	}
	
	@Override
	public void run() {
		if( override || !Brain.variables.globalUserIndex.get(user.getLongID()).userData.containsKey(key) ) {
			Brain.variables.globalUserIndex.get(user.getLongID()).userData.put(key, value);
		}
	}

}
