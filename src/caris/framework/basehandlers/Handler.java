package caris.framework.basehandlers;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basereactions.NullReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public abstract class Handler {
	public static List<String> categories = new ArrayList<String>();
	
	public String name;
	public boolean allowBots;
	
	public String invocation;
	
	public Handler() {
		Module self = this.getClass().getAnnotation(Module.class);
		name = self.name();
		allowBots = self.allowBots();
		
		this.invocation = Constants.INVOCATION_PREFIX + name;
		
		Help helpAnnotation = this.getClass().getAnnotation(Help.class);
		if( helpAnnotation != null ) {
			if( !StringUtilities.containsIgnoreCase(categories, helpAnnotation.category())) {
				categories.add(helpAnnotation.category());
			}
		}
		
		Logger.debug("Handler " + name + " initialized.", 1);
	}
	
	protected boolean botFilter(Event event) {
		return isBot(event) && !allowBots;
	}
	
	protected boolean isBot(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			if( ((MessageReceivedEvent) event).getAuthor().isBot() ) {
				return true;
			}
		}
		return false;
	}
	
	public Reaction onStartup() {
		return new NullReaction();
	}
	
	public abstract Reaction handle(Event event);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Module {
		String name();
		boolean allowBots() default false;
	}
}
