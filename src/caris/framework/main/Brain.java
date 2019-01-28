package caris.framework.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.reflections.Reflections;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.InteractiveHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.events.EventManager;
import caris.framework.events.TimedEventManager;
import caris.framework.library.JSONable.JSONReloadException;
import caris.framework.library.Variables;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import caris.framework.utilities.SaveDataUtilities;
import sx.blah.discord.api.IDiscordClient;


public class Brain {

	/* Variable Library */
	public static Variables variables;
	
	/* Handlers */
	public static Map<String, Handler> handlers = new HashMap<String, Handler>();
	
	/* Interactives */
	public static List<InteractiveHandler> interactives = new ArrayList<InteractiveHandler>();

	/* Event Managers */
	public static EventManager eventManager = new EventManager();

	public static IDiscordClient cli = null;

	public static boolean emptyReported = true;
	
	/* Synchronized */
	public static List<Thread> threadQueue = Collections.synchronizedList(new ArrayList<Thread>());
	public static ConcurrentHashMap<Long, List<Reaction>> timedQueue = new ConcurrentHashMap<Long, List<Reaction>>();
	public static AtomicInteger threadCount = new AtomicInteger(0);
	
	public static void main(String[] args) {

		init();

		if (!(args.length >= 1)) {
			Logger.error("Please pass the TOKEN as the first argument.");
			Logger.error("# java -jar SimpleResponder.jar TOKEN");
			System.exit(0);
		}

		// Gets token from arguments
		String token = args[0];

		cli = BotUtils.getBuiltDiscordClient(token);
		Logger.print("Client built successfully.");

		cli.getDispatcher().registerListener( eventManager );
		Logger.print("Listener established successfully.");

		// Only login after all event registering is done
		cli.login();
		Logger.print("Client logged in.");
		Logger.print("Loaded Channel Map.");

		while( !cli.isReady() ) {
			// Wait to do anything else
		}

		cli.changePlayingText(Constants.INVOCATION_PREFIX + "Help");
		cli.changeUsername(Constants.NAME);
		
		while( true ) {
			iterate();
		}
	}
	
	public static void iterate() { // do things. nothing gets past this block.
		if( !threadQueue.isEmpty() ) {
			emptyReported = false;
			Logger.debug("Threads in queue: " + threadQueue.size(), true);
			try {
				threadQueue.remove(0).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if( !emptyReported ) {
			Logger.debug("Thread queue empty.", true);
			emptyReported = true;
		}
	}
	
	public static void init() { // add handlers to their appropriate categories here
		try {
			variables = new Variables(SaveDataUtilities.JSONIn("tmp/variables.json"));
		} catch (JSONReloadException e) {
			e.printStackTrace();
			variables = new Variables();
		}
		
		Logger.print("Initializing.");
		
		// Load modules
		Logger.print("Loading Handlers...", 1);
		// Load default handlers
		Reflections reflect = new Reflections("caris.framework.handlers");
		for( Class<?> c : reflect.getSubTypesOf( caris.framework.basehandlers.Handler.class ) ) {
			Handler h = null;
			try {
				h = (Handler) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if( h != null ) {
				Logger.print("Adding " + h.name + " to the Handler Map", 2);
				handlers.put( h.name.toLowerCase(), h );
			}
		}
		// Load modular handlers
		reflect = new Reflections("caris.modular.handlers");
		for( Class<?> c : reflect.getSubTypesOf( caris.framework.basehandlers.Handler.class ) ) {
			Handler h = null;
			try {
				h = (Handler) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if( h != null ) {
				Logger.print("Adding " + h.name + " to the Handler Map", 2);
				handlers.put( h.name.toLowerCase(), h );
			}
		}
		
		Logger.print("Loaded Handlers:", 1);
		for( String s : handlers.keySet() ) {
			Logger.print(s, 2);
		}
		
		TimedEventManager timedEvents = new TimedEventManager();
		timedEvents.start();
				
		Logger.print("Initialization complete.");
	}
}
