package caris.framework.library;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.obj.IUser;

public class GlobalUserInfo implements JSONable {

	/* Basic Information */
	public Long userID;
	public int balance;
	
	/* Modular Information */
	public JSONObject userData;
	
	public GlobalUserInfo( JSONObject json ) throws JSONReloadException {
		this();
		if( json != null ) {
			try {
				userID = json.getLong("userID");
			} catch (JSONException e){
				e.printStackTrace();
				throw new JSONReloadException();
			}
			try {
				balance = json.getInt("balance");
			} catch (JSONException e){
				e.printStackTrace();
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
	
	public GlobalUserInfo(IUser user) {
		this();
		this.userID = user.getLongID();
	}
	
	private GlobalUserInfo() {
		balance = 1000;
		userData = new JSONObject();
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONData.put("userData", userData);
		JSONData.put("balance", balance);
		JSONData.put("userID", userID);
		return JSONData;
	}
	
}
