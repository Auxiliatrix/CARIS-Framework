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
import sx.blah.discord.handle.impl.events.guild.GuildEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public abstract class Handler {

	public static List<String> categories = new ArrayList<String>();
	
	public String name;
	public String invocation;

	protected boolean allowBots;
	protected boolean whitelist;
	protected boolean root;
		
	protected List<Long> disabledGuilds;
	
	public Handler() {
		Module self = this.getClass().getAnnotation(Module.class);
		name = self.name();
		allowBots = self.allowBots();
		whitelist = self.whitelist();
		root = self.root();
		
		this.invocation = Constants.INVOCATION_PREFIX + name;
		
		Help helpAnnotation = this.getClass().getAnnotation(Help.class);
		if( helpAnnotation != null ) {
			if( !StringUtilities.containsIgnoreCase(categories, helpAnnotation.category())) {
				categories.add(helpAnnotation.category());
			}
		}
		
		disabledGuilds = new ArrayList<Long>();
		
		Logger.debug("Handler " + name + " initialized.", 1);
	}
	
	public final boolean isRoot() {
		return root;
	}
	
	protected final boolean disableFilter(Event event) {
		if( whitelist ) {
			return event instanceof GuildEvent && !disabledGuilds.contains(((GuildEvent) event).getGuild().getLongID());
		} else {
			return event instanceof GuildEvent && disabledGuilds.contains(((GuildEvent) event).getGuild().getLongID());
		}
	}
	
	public final boolean disabledOn(Long id) {
		return !whitelist && disabledGuilds.contains(id) || !root && whitelist && !disabledGuilds.contains(id);
	}
	
	public final void disableOn(Long id) {
		if( whitelist ) {
			disabledGuilds.remove(id);
		} else {
			if( !root ) {
				if( !disabledGuilds.contains(id) ) {
					disabledGuilds.add(id);
				}
			}
		}
	}
	
	public final void enableOn(Long id) {
		if( whitelist ) {
			if( !root ) {
				if( !disabledGuilds.contains(id) ) {
					disabledGuilds.add(id);
				}
			}
		} else {
			disabledGuilds.remove(id);
		}
	}
	
	protected final boolean isBot(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			if( ((MessageReceivedEvent) event).getAuthor().isBot() ) {
				return true;
			}
		}
		return false;
	}
	
	protected final boolean botFilter(Event event) {
		return isBot(event) && !allowBots;
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
		boolean whitelist() default false;
		boolean root() default false;
	}
}
