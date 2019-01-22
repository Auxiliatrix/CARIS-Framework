package caris.framework.library;

import java.util.HashMap;

import sx.blah.discord.handle.obj.IUser;

public class UserInfo {
	
	/* Basic Information */
	public IUser user;
	
	/* Modular Information */
	public HashMap< String, Object > userData;
	
	public UserInfo( IUser user ) {
		this.user = user;
		
		userData = new HashMap<String, Object>();
	}	
}
