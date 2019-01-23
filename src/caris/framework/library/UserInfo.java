package caris.framework.library;

import java.util.HashMap;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IUser;

public class UserInfo implements JSONable {
	
	/* Basic Information */
	public IUser user;
	
	/* Modular Information */
	public HashMap<String, Object> userData;
	
	public UserInfo( IUser user ) {
		this.user = user;
		
		userData = new HashMap<String, Object>();
	}

	@Override
	public JSONObject getJSONData() {
		HashMap<String, JSONObject> JSONData = new HashMap<String, JSONObject>();
		JSONData.put("userData", JSONify(userData));
		return JSONify(userData);
	}

}
