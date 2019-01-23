package caris.framework.library;

import java.util.HashMap;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Variables {
	// Dynamic global variables
	public static HashMap<String, Object> variableData = new HashMap<String, Object>();
	
	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	
	/* Global UserData */
	public static HashMap<IUser, GlobalUserInfo> globalUserInfo = new HashMap<IUser, GlobalUserInfo>();
}
