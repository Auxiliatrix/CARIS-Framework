package caris.framework.library;

import java.util.HashMap;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IUser;

public class GlobalUserInfo implements JSONable {

	/* Basic Information */
	IUser user;
	public int balance;
	
	/* Modular Information */
	public HashMap<String, Object> userData;
	
	public GlobalUserInfo(IUser user) {
		this.user = user;
		balance = 1000;
		
		userData = new HashMap<String, Object>();
	}

	@Override
	public JSONObject getJSONData() {
		HashMap<String, JSONObject> JSONData = new HashMap<String, JSONObject>();
		JSONData.put("userData", JSONify(userData));
		JSONData.put("balance", new JSONObject(balance));
		return new JSONObject(JSONData);
	}
	
}
