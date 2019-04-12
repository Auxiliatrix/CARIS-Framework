package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;
import sx.blah.discord.handle.obj.IGuild;

public class ScriptModule extends MessageHandler {

	private boolean passive;
	private IGuild guild;
	private Executable code;
	private String source;
		
	public ScriptModule(String name, boolean passive, IGuild guild, Executable code, String source) {
		super(name, true, false, false);
		this.passive = passive;
		this.guild = guild;
		this.code = code;
		this.source = source;
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew) || passive;
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		return code.execute(mew, new Context());
	}
	
	public final IGuild getGuild() {
		return guild;
	}
	
	public final boolean getPassive() {
		return passive;
	}
	
	public final String getSource() {
		return source;
	}
	
}
