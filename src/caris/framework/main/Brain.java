package caris.framework.main;

import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

import caris.configuration.calibration.Constants;
import caris.framework.events.SuperEvent;
import caris.framework.listeners.Listener;
import caris.framework.listeners.ListenerMessageReceived;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class Brain {

	public static IDiscordClient cli = null;
	
	public static String prefix;
	public static String name;
	
	public static Logger logger = new Logger()
										.addOrigin("main");
	
	@SuppressWarnings("rawtypes")
	public static List<Listener> listeners = new ArrayList<Listener>();
	
	public static void main(String[] args) {
		
		if (!(args.length >= 1)) {
			logger.report("Please pass the TOKEN as the first argument.");
			logger.report("# java -jar CARIS.jar TOKEN");
			System.exit(0);
		} else if (!(args.length >= 2)) {
			logger.report("No name passed as the second argument.");
			logger.report("Inferring the default name of \"" + Constants.DEFAULT_NAME + "\".");
			
			name = Constants.DEFAULT_NAME;
		} else {
			name = args[1];
		}
		
		prefix = Constants.INVOCATION_PREFIX;
		
		startup();
		
		login(args[0]);
	}

	private static void startup() {
		Logger startupLogger = logger.clone().addOrigin("Startup");
		startupLogger.log("Initializing");
		
		Reflections listenerClasses = new Reflections(Constants.SOURCE_PACKAGE);
		for( Class<?> c : listenerClasses.getSubTypesOf(caris.framework.listeners.Listener.class) ) {
			try {
				Listener<?> listener = (Listener<?>) c.newInstance();
				listeners.add(listener);
				startupLogger.log("Listener [" + listener.getName() + "] initialized");
			} catch (InstantiationException | IllegalAccessException e) {
				startupLogger.report("Initialization failure.");
			}
		}
	}
	
	private static void login(String token) {
		Logger loginLogger = logger.clone().addOrigin("Login");
		
		loginLogger.log("Logging in");
		
		cli = getBuiltDiscordClient(token);
		loginLogger.log("Client built successfully.");

		for( Listener<?> listener : listeners ) {
			cli.getDispatcher().registerListener(listener);
		}
		loginLogger.log("Listeners registered successfully.");

		cli.login();
		loginLogger.log("Client logged in.");
		
		while( !cli.isReady() ) {}
		loginLogger.log("Client ready!");
		
		cli.changePlayingText(Constants.INVOCATION_PREFIX + "Help");
		cli.changeUsername(name);
		loginLogger.log("Login complete.");
	}

	private static IDiscordClient getBuiltDiscordClient(String token) {
		return new ClientBuilder()
				.withToken(token)
				.withRecommendedShardCount()
				.build();
	}
	
}
