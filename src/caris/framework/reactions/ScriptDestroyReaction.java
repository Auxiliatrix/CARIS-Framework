package caris.framework.reactions;

import caris.framework.basehandlers.ScriptModule;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;

public class ScriptDestroyReaction extends Reaction {

	private ScriptModule script;
	
	public ScriptDestroyReaction(ScriptModule dynamic) {
		this.script = dynamic;
	}
	
	@Override
	public void process() {
		Brain.scripts.remove(script.name);
		if( script.getGuild() == null ) {
			Brain.variables.atomicVariableData.get().getJSONObject("scripts").remove(script.name);
		} else {
			Brain.variables.getGuildInfo(script.getGuild()).guildData.getJSONObject("scripts").remove(script.name);
		}
	}
	
}
