package caris.framework.utilities;

import caris.configuration.calibration.LoggerSettings;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Logger {
		
	public static void say(String message, IChannel channel) {
		if( LoggerSettings.SAY ) {
			String output = "(" + channel.getLongID() + ") <" + channel.getName() + ">: " + message;
			consolePrint(output);
			log(output);
		}
	}
	
	public static void hear(String message, IUser user, IChannel channel) {
		if( LoggerSettings.HEAR ) {
			String output = "(" + channel.getLongID() + ") <";
			output += channel.getName() + "> [";
			output += user.getName() + "]: ";
			output += message;
			consolePrint(output);
			log(output);
		}
	}
	
	public static void error(String message) {
		error(message, LoggerSettings.DEFAULT_INDENT_LEVEL);
	}
	
	public static void error(String message, int level) {
		String output = "[ERROR]";
		for( int f=0; f<level*LoggerSettings.DEFAULT_INDENT_INCREMENT; f++ ) {
			output += LoggerSettings.ERROR_INDENT;
		}
		output += LoggerSettings.HEADER;
		output += message;
		System.err.println(output);
		if( LoggerSettings.LOG ) {
			log(output);
		}
	}
	
	public static void debug(String message) {
		debug(message, LoggerSettings.DEFAULT_INDENT_LEVEL, false);
	}
	
	public static void debug(String message, boolean verbose) {
		debug(message, LoggerSettings.DEFAULT_INDENT_LEVEL, verbose);
	}
	
	public static void debug(String message, int level) {
		debug(message, level, false);
	}
	
	@SuppressWarnings("unused")
	public static void debug(String message, int level, boolean verbose) {
		if( (LoggerSettings.DEBUG_LEVEL == -1 || LoggerSettings.DEBUG_LEVEL >= level) && (!verbose || LoggerSettings.VERBOSE) ) {
			String output = "[DEBUG]";
			if( LoggerSettings.DEBUG ) {
				for( int f=0; f<level*LoggerSettings.DEFAULT_INDENT_INCREMENT; f++ ) {
					output += LoggerSettings.DEBUG_INDENT;
				}
				output += LoggerSettings.HEADER;
				output += message;
				consolePrint(output);
				if( LoggerSettings.LOG && ( LoggerSettings.LOG_LEVEL == -1 || LoggerSettings.LOG_LEVEL >= level )) {
					log(output);
				}
			}
		}
	}
	
	public static void print(String message) {
		print(message, LoggerSettings.DEFAULT_INDENT_LEVEL);
	}
	
	public static void print(String message, boolean verbose) {
		print(message, LoggerSettings.DEFAULT_INDENT_LEVEL, verbose);
	}
	
	public static void print(String message, int level) {
		print(message, level, false);
	}
	
	@SuppressWarnings("unused")
	public static void print(String message, int level, boolean verbose) {
		if( (LoggerSettings.PRINT_LEVEL == -1 || LoggerSettings.PRINT_LEVEL >= level) && (!verbose || LoggerSettings.VERBOSE)) {
			String output = "[PRINT]";
			if( LoggerSettings.PRINT ) {
				for( int f=0; f<level*LoggerSettings.DEFAULT_INDENT_INCREMENT; f++ ) {
					output += LoggerSettings.PRINT_INDENT;
				}
				output += LoggerSettings.HEADER;
				output += message;
				consolePrint(output);
				if( LoggerSettings.LOG && ( LoggerSettings.LOG_LEVEL == -1 || LoggerSettings.LOG_LEVEL >= level )) {
					log(output);
				}
			}
		}
	}
	
	public static void log(String message) {
//		BufferedWriter logWriter;
//		try {
//			logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
//					new File( ( LoggerSettings.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + LoggerSettings.LOG_FILE_NAME + LoggerSettings.SAVEEXTENTION ) ), LoggerSettings.ENCODING));
//			logWriter.write(message);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public static void consolePrint(String message) {
		System.out.println(message);
		if( Brain.cli != null ) {
			for( IGuild guild : Brain.cli.getGuilds() ) {
				BotUtils.sendLog(guild, message);
			}
		}
	}
}
