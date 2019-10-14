package caris.framework.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.reflections.Reflections;

import caris.common.calibration.Constants;
import caris.framework.data.GlobalUserData;
import caris.framework.data.GuildData;
import caris.framework.data.JSONable.JSONReloadException;
import caris.framework.listeners.Listener;
import caris.framework.utilities.Logger;
import caris.framework.utilities.SaveDataUtilities;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class Brain {

	public static IDiscordClient cli = null;
		
	public static String prefix;
	public static String name;
	
	public static Logger logger = new Logger().addOrigin("main");
	
	@SuppressWarnings("rawtypes")
	public static List<Listener> listeners = new ArrayList<Listener>();
	
	public static Map<Long, GlobalUserData> globalUserDataMap = new HashMap<Long, GlobalUserData>();
	public static Map<Long, GuildData> guildDataMap = new HashMap<Long, GuildData>();
	
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
		
		reload();
		
		startup();
		
		login(args[0]);
	}

	private static void reload() {
		Logger reloadLogger = logger.clone().addOrigin("Reload");
		reloadLogger.log("Initializing");
		
		File memory = new File(Constants.FOLDER_MEMORY);
		if( !memory.exists() ) {
			reloadLogger.log("Memory Folder uninitialized");
			memory.mkdir();
			reloadLogger.log("New Memory Folder generated");
		}
		
		final String globalUserDataPath = Constants.FOLDER_MEMORY + File.separator + Constants.SUBFOLDER_GLOBALUSERDATA;
		
		File directory = new File(globalUserDataPath);
		if( !directory.exists() ) {
			reloadLogger.log("GlobalUserData Folder uninitialized");
			directory.mkdir();
			reloadLogger.log("GlobalUserData Folder generated");
		}
		File[] files = directory.listFiles();
		if( files != null ) {
			for( File file : files ) {
				JSONObject object = SaveDataUtilities.JSONIn(globalUserDataPath + File.separator + file.getName());
				try {
					GlobalUserData globalUserData = new GlobalUserData(object);
					globalUserDataMap.put(globalUserData.getUserID(), globalUserData);
				} catch (JSONReloadException e) {
					reloadLogger.report("Corrupted GlobalUserData file: " + globalUserDataPath + File.separator + file.getName());
				}
			}
		} else {
			reloadLogger.report("The expected memory folder is a file instead.");
		}
		
		final String guildDataPath = Constants.FOLDER_MEMORY + File.separator + Constants.SUBFOLDER_GUILDDATA;
		
		directory = new File(guildDataPath);
		if( !directory.exists() ) {
			reloadLogger.log("GuildData Folder uninitialized");
			directory.mkdir();
			reloadLogger.log("GuildData Folder generated");
		}
		files = directory.listFiles();
		if( files != null ) {
			for( File file : files ) {
				JSONObject object = SaveDataUtilities.JSONIn(guildDataPath + File.separator + file.getName() + File.separator + "_data.json");
				try {
					GuildData guildData = new GuildData(object);
					guildDataMap.put(guildData.getGuildID(), guildData);
				} catch (JSONReloadException e) {
					reloadLogger.report("Corrupted GuildData file: " + guildDataPath + File.separator + file.getName()+ File.separator + "_data.json");
				}
			}
		} else {
			reloadLogger.report("The expected memory folder is a file instead.");
		}
		
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
		
		cli.changePlayingText(prefix + "Help");
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
