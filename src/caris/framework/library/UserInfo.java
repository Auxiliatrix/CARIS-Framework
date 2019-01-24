package caris.framework.library;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IUser;

public class UserInfo implements JSONable {
	
	/* Basic Information */
	public IUser user;
	
	/* Modular Information */
	public JSONObject userData;
	
	public UserInfo( IUser user ) {
		this.user = user;
		
		userData = new JSONObject();
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONData.put("userData", userData);
		return JSONData;
	}

}
