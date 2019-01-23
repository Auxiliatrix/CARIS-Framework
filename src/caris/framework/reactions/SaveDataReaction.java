package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.SaveDataUtilities;

public class SaveDataReaction extends Reaction {

	public SaveDataReaction() {
		super(-1);
	}
	
	@Override
	public void run() {
		String result = SaveDataUtilities.JSONOut("tmp/variables.json", Brain.variables.getJSONData());
		System.out.println(result);
	}
	
}
