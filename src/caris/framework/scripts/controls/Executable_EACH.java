package caris.framework.scripts.controls;

import caris.framework.basereactions.QueueReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.Executable;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Executable_EACH extends Executable {

	private String iterable;
	private Executable body;
	
	public Executable_EACH(String iterable, Executable body) {
		this.iterable = iterable;
		this.body = body;
	}
	
	@Override
	public Reaction execute(MessageEventWrapper mew, Context context) {
		QueueReaction execution = new QueueReaction();
		switch(iterable.charAt(1)) {
			case '$':
				for( String element : ScriptCompiler.resolveStringIterable(mew, context, iterable) ) {
					Context newContext = new Context(context);
					newContext.putString(iterable.substring(1, iterable.indexOf("{")), element);
					Reaction reaction = body.execute(mew, newContext);
					if( reaction == null ) {
						break;
					}
					execution.add(reaction);
				}
				break;
			case '#':
				for( int element : ScriptCompiler.resolveIntIterable(mew, context, iterable) ) {
					Context newContext = new Context(context);
					newContext.putInt(iterable.substring(1, iterable.indexOf("{")), element);
					Reaction reaction = body.execute(mew, newContext);
					if( reaction == null ) {
						break;
					}
					execution.add(reaction);
				}
				break;
			case '@':
				if( iterable.charAt(2) == 'R' ) {
					for( IRole element : ScriptCompiler.resolveRoleIterable(mew, context, iterable) ) {
						Context newContext = new Context(context);
						newContext.putRole(iterable.substring(1, iterable.indexOf("{")), element);
						Reaction reaction = body.execute(mew, newContext);
						if( reaction == null ) {
							break;
						}
						execution.add(reaction);
					}
				} else {
					for( IUser element : ScriptCompiler.resolveUserIterable(mew, context, iterable) ) {
						Context newContext = new Context(context);
						newContext.putUser(iterable.substring(1, iterable.indexOf("{")), element);
						Reaction reaction = body.execute(mew, newContext);
						if( reaction == null ) {
							break;
						}
						execution.add(reaction);
					}
				}
				break;
			default:
				return null;
		}
		return execution;
	}

}
