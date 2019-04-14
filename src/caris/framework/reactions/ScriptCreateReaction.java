package caris.framework.reactions;

import caris.framework.basehandlers.ScriptModule;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;

public class ScriptCreateReaction extends Reaction {

	private ScriptModule script;
	
	public ScriptCreateReaction(ScriptModule dynamic) {
		this.script = dynamic;
	}

	@Override
	public void process() {
		Brain.scripts.put(script.name, script);
		if( script.getGuild() == null ) {
			Brain.variables.atomicVariableData.get().getJSONObject("scripts").put(script.name, script.getSource());
		} else {
			System.out.print("stored");
			Brain.variables.getGuildInfo(script.getGuild()).guildData.getJSONObject("scripts").put(script.name, script.getSource());
		}
	}
	
}
