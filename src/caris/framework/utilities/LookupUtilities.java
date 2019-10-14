package caris.framework.utilities;

import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IUser;

public class LookupUtilities {

	public static String getFullUserNameByID(long id) {
		IUser user = Brain.cli.getUserByID(id);
		return user.getName() + "#" + user.getDiscriminator();
	}
	
	public static String getGuildNameByID(long id) {
		return Brain.cli.getGuildByID(id).getName();
	}
	
}
