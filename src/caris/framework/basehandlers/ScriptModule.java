package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Context;
import caris.framework.scripts.Executable;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

public class ScriptModule extends MessageHandler {

	private String source;
	private Executable code;
	
	private IGuild guild;
	private boolean passive;
	
	public ScriptModule(String header, Executable code, String source, IGuild guild, Permissions[] requirements) {
		super(header.substring(1, header.length()-1), true, false, false, requirements);
		
		this.source = source;
		
		switch (header.charAt(0)) {
			case '%':
				this.guild = null;
				passive = false;
				break;
			case '=':
				this.guild = null;
				passive = true;
				break;
			case '+':
				this.guild = guild;
				passive = false;
				break;
			case '-':
				this.guild = guild;
				passive = true;
				break;
			default:
				throw new AssertionError("Invalid header!");
		}
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
