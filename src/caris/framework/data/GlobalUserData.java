package caris.framework.data;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IUser;

public class GlobalUserData implements JSONable {
	
	private Long userID;
	private JSONObject data;
	
	public GlobalUserData(long id) {
		data = new JSONObject();
		userID = id;
		data.put("userID", id);
	}
	
	public GlobalUserData(IUser user) {
		data = new JSONObject();
		userID = user.getLongID();
		data.put("userID", user.getLongID());
	}

	public GlobalUserData( JSONObject json ) throws JSONReloadException {
		if( !json.has("userID") || !(json.get("userID") instanceof Long) ) {
			throw new JSONReloadException();
		}
		data = json;
		userID = json.getLong("userID");
	}
	
	public Long getUserID() {
		return userID;
	}
	
	@Override
	public JSONObject getJSONData() {
		return data;
	}

}
