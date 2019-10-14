package caris.framework.reactions;

import org.json.JSONObject;

import caris.common.calibration.Constants;
import caris.framework.data.GlobalUserData;
import caris.framework.data.GuildData;
import caris.framework.main.Brain;
import caris.framework.utilities.LookupUtilities;
import caris.framework.utilities.SaveDataUtilities;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class SaveDataReaction extends Reaction {

	public SaveDataReaction() {
		super(Tag.PASSIVE);
	}
	
	@Override
	protected void task() {
		for( IUser user : Brain.cli.getUsers() ) {
			if( !Brain.globalUserDataMap.containsKey(user.getLongID()) ) {
				Brain.globalUserDataMap.put(user.getLongID(), new GlobalUserData(user));
			}
		}
		for( long key : Brain.globalUserDataMap.keySet() ) {
			JSONObject value = Brain.globalUserDataMap.get(key).getJSONData();
			SaveDataUtilities.JSONCreateAndOut(new String[] {Constants.FOLDER_MEMORY, Constants.SUBFOLDER_GLOBALUSERDATA, LookupUtilities.getFullUserNameByID(key) + "_" + key + ".json"}, value);
		}
		
		for( IGuild guild : Brain.cli.getGuilds() ) {
			if( !Brain.guildDataMap.containsKey(guild.getLongID()) ) {
				Brain.guildDataMap.put(guild.getLongID(), new GuildData(guild));
			}
		}
		for( long key : Brain.guildDataMap.keySet() ) {
			JSONObject value = Brain.guildDataMap.get(key).getJSONData();
			SaveDataUtilities.JSONCreateAndOut(new String[] {Constants.FOLDER_MEMORY, Constants.SUBFOLDER_GUILDDATA,LookupUtilities.getGuildNameByID(key) + "_" + key, "_data" + ".json"}, value);
		}
		
		System.out.println("Saved!");
	}
	
}
