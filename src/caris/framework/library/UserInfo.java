package caris.framework.library;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.obj.IUser;

public class UserInfo implements JSONable {
	
	/* Basic Information */
	public Long userID;
	
	/* Modular Information */
	public JSONObject userData;
	
	public UserInfo( JSONObject json ) throws JSONReloadException {
		this();
		if( json != null ) {
			try {
				userID = json.getLong("userID");
			} catch (JSONException e) {
				e.printStackTrace();
				throw new JSONReloadException();
			}
			try {
				userData = json.getJSONObject("userData");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			throw new JSONReloadException();
		}
	}
	
	public UserInfo( IUser user ) {
		this();
		this.userID = user.getLongID();
	}
	
	private UserInfo() {
		userData = new JSONObject();
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONData.put("userData", userData);
		JSONData.put("userID", userID);
		return JSONData;
	}

}
