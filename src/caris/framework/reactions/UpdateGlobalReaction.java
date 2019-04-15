package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;

public class UpdateGlobalReaction extends Reaction {

	public String key;
	public Object value;
	public boolean override;
	
	public UpdateGlobalReaction(String key, Object value) {
		this(key, value, false, -1);
	}
	
	public UpdateGlobalReaction(String key, Object value, boolean override) {
		this(key, value, override, -1);
	}
	
	public UpdateGlobalReaction(String key, Object value, int priority) {
		this(key, value, false, priority);
	}
	
	public UpdateGlobalReaction(String key, Object value, boolean override, int priority) {
		super(priority);
		this.key = key;
		this.value = value;
		this.override = override;
	}
	
	@Override
	public void process() {
		if( !Brain.variables.atomicVariableData.get().has(key) || override ) {
			if( value == null ) {
				Brain.variables.atomicVariableData.get().remove(key);
			} else {
				Brain.variables.atomicVariableData.get().put(key, value);
			}
		}
	}

}
