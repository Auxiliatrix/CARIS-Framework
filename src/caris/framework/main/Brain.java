package caris.framework.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.reflections.Reflections;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.InteractiveModule;
import caris.framework.basereactions.Reaction;
import caris.framework.events.EventManager;
import caris.framework.events.TimedEventManager;
import caris.framework.library.JSONable.JSONReloadException;
import caris.framework.library.Variables;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import caris.framework.utilities.SaveDataUtilities;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;


public class Brain {

	/* Variable Library */
	public static Variables variables;
	
	/* Handlers */
	public static Map<String, Handler> modules = new HashMap<String, Handler>();
	
	/* Event Managers */
	public static EventManager eventManager = new EventManager();

	public static IDiscordClient cli = null;
	
	/* Synchronized */
	public static List<Thread> threadQueue = Collections.synchronizedList(new ArrayList<Thread>());
	public static ConcurrentHashMap<Reaction, Long> timedQueue = new ConcurrentHashMap<Reaction, Long>();
	public static AtomicInteger threadCount = new AtomicInteger(0);
	public static List<InteractiveModule> interactives = Collections.synchronizedList(new ArrayList<InteractiveModule>());
	
	public static void main(String[] args) {
		if (!(args.length >= 1)) {
			Logger.error("Please pass the TOKEN as the first argument.");
			Logger.error("# java -jar SimpleResponder.jar TOKEN");
			System.exit(0);
		}
		
		setup();
		login(args[0]);
		startup();
		
		while( true ) {
			iterate();
		}
	}
	
	private static void setup() {
		try {
			variables = new Variables(SaveDataUtilities.JSONIn("tmp/variables.json"));
		} catch (JSONReloadException e) {
			e.printStackTrace();
			variables = new Variables();
		}
		
		Logger.print("Setting up...");
		
		loadModules("caris.framework.handlers");
		
		for( String packagePrefix : Constants.MODULE_PACKAGES ) {
			loadModules(packagePrefix, "caris.framework.handlers");
		}
		
		Logger.print("Setup complete.");
	}
	
	private static void login(String token) {
		Logger.print("Logging in...");
		
		cli = BotUtils.getBuiltDiscordClient(token);
		Logger.print("Client built successfully.", 1);

		cli.getDispatcher().registerListener( eventManager );
		Logger.print("Listener established successfully.", 1);

		cli.login();
		Logger.print("Client logged in.", 1);
		Logger.print("Loaded Channel Map.", 1);
		
		while( !cli.isReady() ) {}
		Logger.print("Client ready!", 1);
		
		cli.changePlayingText(Constants.INVOCATION_PREFIX + "Help");
		// cli.changeUsername(Constants.NAME);
		Logger.print("Log in complete.");
	}
	
	private static void startup() {
		Logger.print("Starting up...");
		
		TimedEventManager timedEvents = new TimedEventManager();
		timedEvents.start();
		Logger.print("Time started successfully.", 1);
		
		for( IGuild guild : cli.getGuilds() ) {
			variables.addGuild(guild);
		}
		Logger.print("Guilds loaded successfully.", 1);
		
		for( String name : modules.keySet() ) {
			Handler h = modules.get(name);
			h.onStartup().start();
		}
		Logger.print("Handler startup protocols executed successfully.", 1);
		
		Logger.print("Startup complete.");
	}
	
	private static void iterate() {
		if( !threadQueue.isEmpty() ) {
			Logger.debug("Threads in queue: " + threadQueue.size(), true);
			try {
				threadQueue.remove(0).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void loadModules(String includePrefix) {
		loadModules(includePrefix, "");
	}
	
	private static void loadModules(String includePrefix, String excludePrefix) {
		Logger.print("Loading Handlers from \"" + includePrefix + "\":");
		Reflections include = new Reflections(includePrefix);
		Reflections exclude = excludePrefix.isEmpty() ? null : new Reflections(excludePrefix);
		for( Class<?> c : include.getSubTypesOf(caris.framework.basehandlers.Handler.class) ) {
			if( exclude != null && exclude.getSubTypesOf(caris.framework.basehandlers.Handler.class).contains(c) ) {
				continue;
			}
			Module moduleAnnotation = c.getAnnotation(Module.class);
			if( moduleAnnotation != null ) {
				Logger.print("Adding Handler " + moduleAnnotation.name() + " to the Module map.", 1);
				if( modules.containsKey(moduleAnnotation.name().toLowerCase()) ) {
					Logger.print("Handler with this name already exists. Overriding.", 2);
				}
				try {
					modules.put( moduleAnnotation.name().toLowerCase(), (Handler) c.newInstance() );
				} catch (InstantiationException e) {
					Logger.error("Failed to load Handler " + moduleAnnotation.name() + " (InstantiationException)", 2);
				} catch (IllegalAccessException e) {
					Logger.error("Failed to load Handler " + moduleAnnotation.name() + " (IllegalAccessException)", 2);
				}
			}
		}
	}
}
