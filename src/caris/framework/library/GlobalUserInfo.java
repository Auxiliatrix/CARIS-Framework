package caris.framework.library;

import java.util.HashMap;

import sx.blah.discord.handle.obj.IUser;

public class GlobalUserInfo {

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
	
}
