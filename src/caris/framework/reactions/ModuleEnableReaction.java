package caris.framework.reactions;

import caris.framework.basehandlers.Handler;
import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IGuild;

public class ModuleEnableReaction extends Reaction {
	
	private IGuild guild;
	private Handler handler;
	
	public ModuleEnableReaction(IGuild guild, Handler handler) {
		this(guild, handler, 0);
	}
	
	public ModuleEnableReaction(IGuild guild, Handler handler, int priority) {
		super(priority);
		this.guild = guild;
		this.handler = handler;
	}

	@Override
	public void process() {
		handler.enableOn(guild.getLongID());
	}

}
