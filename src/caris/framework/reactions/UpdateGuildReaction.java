package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IGuild;

public class UpdateGuildReaction extends Reaction {

	public IGuild guild;
	public String key;
	public Object value;
	public boolean override;
	
	public UpdateGuildReaction(IGuild guild, String key, Object value) {
		this(guild, key, value, false, -1);
	}
	
	public UpdateGuildReaction(IGuild guild, String key, Object value, boolean override) {
		this(guild, key, value, override, -1);
	}
	
	public UpdateGuildReaction(IGuild guild, String key, Object value, int priority) {
		this(guild, key, value, false, priority);
	}
	
	public UpdateGuildReaction(IGuild guild, String key, Object value, boolean override, int priority) {
		super(priority);
		this.guild = guild;
		this.key = key;
		this.value = value;
		this.override = override;
	}
	
	@Override
	public void process() {
		if( !Brain.variables.getGuildInfo(guild).guildData.has(key) || override ) {
			if( value == null ) {
				Brain.variables.getGuildInfo(guild).guildData.remove(key);
			} else {
				Brain.variables.getGuildInfo(guild).guildData.put(key, value);
			}
		}
	}

}
