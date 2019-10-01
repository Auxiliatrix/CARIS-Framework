package caris.framework.reactions;

import java.io.File;

import org.json.JSONObject;

import caris.common.calibration.Constants;
import caris.framework.data.GlobalUserData;
import caris.framework.main.Brain;
import caris.framework.utilities.SaveDataUtilities;
import sx.blah.discord.handle.obj.IUser;

public class SaveDataReaction extends Reaction {

	public SaveDataReaction() {
		super(Tag.PASSIVE);
	}
	
	@Override
	protected void task() {
		for( IUser user : Brain.cli.getUsers() ) {
			if( !Brain.globalUserDataMap.containsKey(user.getLongID()) ) {
				Brain.globalUserDataMap.put(user.getLongID(), new GlobalUserData(user.getLongID()));
			}
		}
		for( long key : Brain.globalUserDataMap.keySet() ) {
			JSONObject value = Brain.globalUserDataMap.get(key).getJSONData();
			SaveDataUtilities.JSONOut(Constants.FOLDER_MEMORY + File.separator + Constants.SUBFOLDER_GLOBALUSERDATA + File.separator + key + ".json", value);
		}
		System.out.println("Saved!");
	}
	
}
