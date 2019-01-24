package caris.framework.library;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IUser;

public class GlobalUserInfo implements JSONable {

	/* Basic Information */
	IUser user;
	public int balance;
	
	/* Modular Information */
	public JSONObject userData;
	
	public GlobalUserInfo(IUser user) {
		this.user = user;
		balance = 1000;
		
		userData = new JSONObject();
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONData.put("userData", userData);
		JSONData.put("balance", balance);
		return JSONData;
	}
	
}
