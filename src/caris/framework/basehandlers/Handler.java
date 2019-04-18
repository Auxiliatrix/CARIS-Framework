package caris.framework.basehandlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.MessageHandler.Command;
import caris.framework.basereactions.NullReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.GuildEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * The Handler class represents the base Module class used throughout the CARIS Framework.
 * Handlers are referenced in the case of a specific Event in the {@link caris.framework.events.EventManager#onEvent(Event)} function.
 * Extensions of this object include a {@link #handle(Event)} function, which returns an executable {@link caris.framework.basereactions.Reaction} object.
 * In addition, Handler classes should be annotated with the {@link caris.framework.basehandlers.Handler.Module} annotation.
 * @author Alina Kim
 *
 */
public abstract class Handler {

	/**
	 * A static Set of all categories that Handlers can belong to.
	 * @see caris.framework.embedbuilders.HelpBuilder
	 */
	public static Set<String> categories = new HashSet<String>();
	
	/**
	 * The primary name of the Handler.
	 * This is the name that this Handler is stored under, and will appear as in Help documentation.
	 */
	public String name;
	
	/**
	 * The primary invocation to check for.
	 */
	public String invocation;
	
	/**
	 * The set of aliases that are considered to be synonyms of the primary invocation.
	 */
	protected Set<String> aliases;

	/**
	 * Whether to ignore Events that originate from, or are caused by, other Discord Bots.
	 */
	protected boolean allowBots;
	
	/**
	 * Whether this Module should be white-listed.
	 * A white-listed Module is disabled in a Guild by default.
	 */
	protected boolean whitelist;
	
	/**
	 * Whether this Module is exclusive to Developers only.
	 */
	protected boolean root;
	
	/**
	 * The set of IDs of Guilds in which this Module is disabled.
	 * On a white-listed Handler, this set contains the IDs of Guilds in which this Module is enabled instead.
	 */
	protected Set<Long> disabledGuilds;
	
	/**
	 * Constructs a Handler object based on its Annotations.
	 * It loads the {@link #name}, {@link #allowBots}, {@link #whitelist}, and {@link #root} fields from the {@link caris.framework.basehandlers.Handler.Module} annotation.
	 * It loads the {@link #categories} field from the {@link caris.framework.embedbuilders.HelpBuilder.Help} annotation.
	 * It loads the {@link #aliases} field from the {@link caris.framework.basehandlers.MessageHandler.Command} annotation.
	 */
	public Handler() {
		/* Load fields from the Module annotation */
		Module self = this.getClass().getAnnotation(Module.class);
		if( self != null ) {
			name = self.name();
			allowBots = self.allowBots();
			whitelist = self.whitelist();
			root = self.root();
		}
		
		/* Set up initial values */
		this.invocation = Constants.INVOCATION_PREFIX + name;
		this.aliases = new HashSet<String>();
		aliases.add(invocation);
		
		/* Load fields from the Help annotation */
		Help helpAnnotation = this.getClass().getAnnotation(Help.class);
		if( helpAnnotation != null ) {
			if( !StringUtilities.containsIgnoreCase(categories, helpAnnotation.category())) {
				categories.add(helpAnnotation.category());
			}
		}
		
		/* Load fields from the Command annotation */
		Command commandAnnotation = this.getClass().getAnnotation(Command.class);
		if( commandAnnotation != null ) {
			for( String alias : commandAnnotation.aliases() ) {
				aliases.add(Constants.INVOCATION_PREFIX + alias);
			}
		}
		
		disabledGuilds = new HashSet<Long>();
		
		Logger.debug("Handler " + name + " initialized.", 1);
	}
	
	/**
	 * Constructs a Handler object based on parameterized fields, rather than at-runtime annotations.
	 * While Handler objects are generally meant to be uniquely created per class, this Constructor
	 * exists in order to provide a method of dynamic construction of Handler objects when a single class
	 * might need to be constructed repeatedly with varying parameters.
	 * @see caris.framework.basehandlers.ScriptModule for an example.
	 * @param name The name of the Handler being constructed; initializes {@link #name}
	 * @param allowBots	Whether to process Events originating from other Bots; initializes {@link #allowBots}
	 * @param whitelist Whether this Handler should be white-listed; initializes {@link #whitelist}
	 * @param root Whether this Handler is developer-exclusive; initializes {@link #root}
	 */
	protected Handler(String name, boolean allowBots, boolean whitelist, boolean root) {
		/* Initialize fields from parameters */
		this.name = name;
		this.allowBots = allowBots;
		this.whitelist = whitelist;
		this.root = root;
		
		/* Set up initial values */
		this.invocation = Constants.INVOCATION_PREFIX + name;
		this.aliases = new HashSet<String>();
		aliases.add(invocation);
		
		disabledGuilds = new HashSet<Long>();
		
		Logger.debug("Handler " + name + " initialized.", 1);
	}
	
	/**
	 * Getter method that returns the value of {@link #root}
	 * @return boolean Whether this Handler is developer-exclusive
	 */
	public final boolean isRoot() {
		return root;
	}
	
	/**
	 * This method checks whether to ignore an Event because this Handler is disabled on the Guild it originated from.
	 * @param event Event to check
	 * @return boolean Whether to ignore the given Event
	 */
	protected final boolean disableFilter(Event event) {
		if( whitelist ) { // If the Handler is white-listed, then the logic should be flipped
			return event instanceof GuildEvent && !disabledGuilds.contains(((GuildEvent) event).getGuild().getLongID());
		} else {
			return event instanceof GuildEvent && disabledGuilds.contains(((GuildEvent) event).getGuild().getLongID());
		}
	}
	
	/**
	 * This method checks to see whether this Handler is disabled on a given Guild.
	 * @param id long ID of Guild to check
	 * @return Whether this Handler is disabled on the given Guild
	 */
	public final boolean disabledOn(Long id) {
		return !whitelist && disabledGuilds.contains(id) || !root && whitelist && !disabledGuilds.contains(id);
	}
	
	/**
	 * This method disables this Handler on a given Guild.
	 * @param id long ID of Guild to disable on
	 */
	public final void disableOn(Long id) {
		if( whitelist ) { // If the Handler is white-listed, then the logic is flipped
			disabledGuilds.remove(id);
		} else {
			if( !root ) { // Root handlers cannot be disabled
				disabledGuilds.add(id);
			}
		}
	}
	
	/**
	 * This method enables this Handler on a given Guild.
	 * @param id long ID of guild to enable on
	 */
	public final void enableOn(Long id) {
		if( whitelist ) { // If the Handler is white-listed, then the logic if flipped
			disabledGuilds.add(id);
		} else {
			disabledGuilds.remove(id);
		}
	}
	
	/**
	 * This method checks whether a given Event originates from a Bot.
	 * @param event Event to check
	 * @return boolean Whether the given Event originated from a Bot
	 */
	protected final boolean isBot(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			if( ((MessageReceivedEvent) event).getAuthor().isBot() ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Whether to ignore the given Event due to it originating from a Bot.
	 * @param event Event to check
	 * @return boolean Whether to ignore the Event
	 */
	protected final boolean botFilter(Event event) {
		return isBot(event) && !allowBots;
	}
	
	/**
	 * This method is run during startup, and is intended as the set-up instructions for the Handler.
	 * It is meant to be overriden in Handlers that involve set-up operations, such as loading relevant information from a save file.
	 * @see caris.framework.handlers.ScriptHandler
	 * @return Reaction Actions to execute on startup
	 */
	public Reaction onStartup() {
		return new NullReaction();
	}
	
	/**
	 * This method is called in {@link caris.framework.events.EventManager#onEvent(Event)}, and contains all the primary logic in the Handler.
	 * It is meant to be overriden in all extensions.
	 * @param event Event to process
	 * @return Reaction Actions to execute as a result of the processing
	 */
	public abstract Reaction handle(Event event);

	/**
	 * The Module Annotation is used to provide Handler-specific settings at runtime.
	 * @author Alina Kim
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Module {
		String name();
		boolean allowBots() default false;
		boolean whitelist() default false;
		boolean root() default false;
	}
}
