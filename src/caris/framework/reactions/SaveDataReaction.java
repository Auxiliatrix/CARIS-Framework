package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.SerialIOUtilities;

public class SaveDataReaction extends Reaction {

	public SaveDataReaction() {
		super(-1);
	}
	
	@Override
	public void run() {
		SerialIOUtilities.JSONOut("tmp/variableData.json", Variables.variableData);
		SerialIOUtilities.JSONOut("tmp/guildIndex.json", Variables.guildIndex);
		SerialIOUtilities.JSONOut("tmp/globalUserInfo.json", Variables.globalUserInfo);
	}
	
}
