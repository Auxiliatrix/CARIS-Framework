package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public abstract class Handler {

	public boolean enabled;
	
	public String name;
	public boolean allowBots;
		
	public Handler(String name) {
		this(name, false);
	}
	
	public Handler(String name, boolean allowBots) {
		enabled = true;
		
		this.name = name;
		this.allowBots = allowBots;
						
		Logger.debug("Handler " + name + " initialized.", 1);
	}
		
	protected boolean botFilter(Event event) {
		return isBot(event) && !allowBots;
	}
	
	protected boolean isBot(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			if( !Constants.RESPOND_TO_BOT && ((MessageReceivedEvent) event).getAuthor().isBot() ) {
				return true;
			}
		}
		return false;
	}
	
	public abstract Reaction handle(Event event);
	public abstract String getDescription();
	
}
