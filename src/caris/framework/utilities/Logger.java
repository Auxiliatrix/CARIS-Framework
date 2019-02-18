package caris.framework.utilities;

import caris.framework.calibration.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Logger {
		
	public static void say(String message, IChannel channel) {
		if( Constants.SAY ) {
			String output = "(" + channel.getLongID() + ") <" + channel.getName() + ">: " + message;
			consolePrint(output);
			log(output);
		}
	}
	
	public static void hear(String message, IUser user, IChannel channel) {
		if( Constants.HEAR ) {
			String output = "(" + channel.getLongID() + ") <";
			output += channel.getName() + "> [";
			output += user.getName() + "]: ";
			output += message;
			consolePrint(output);
			log(output);
		}
	}
	
	public static void error(String message) {
		error(message, Constants.DEFAULT_INDENT_LEVEL);
	}
	
	public static void error(String message, int level) {
		String output = "[ERROR]";
		for( int f=0; f<level*Constants.DEFAULT_INDENT_INCREMENT; f++ ) {
			output += Constants.ERROR_INDENT;
		}
		output += Constants.HEADER;
		output += message;
		System.err.println(output);
		if( Constants.LOG ) {
			log(output);
		}
	}
	
	public static void debug(String message) {
		debug(message, Constants.DEFAULT_INDENT_LEVEL, false);
	}
	
	public static void debug(String message, boolean verbose) {
		debug(message, Constants.DEFAULT_INDENT_LEVEL, verbose);
	}
	
	public static void debug(String message, int level) {
		debug(message, level, false);
	}
	
	public static void debug(String message, int level, boolean verbose) {
		if( (Constants.DEBUG_LEVEL == -1 || Constants.DEBUG_LEVEL >= level) && (!verbose || Constants.VERBOSE) ) {
			String output = "[DEBUG]";
			if( Constants.DEBUG ) {
				for( int f=0; f<level*Constants.DEFAULT_INDENT_INCREMENT; f++ ) {
					output += Constants.DEBUG_INDENT;
				}
				output += Constants.HEADER;
				output += message;
				consolePrint(output);
				if( Constants.LOG && ( Constants.LOG_LEVEL == -1 || Constants.LOG_LEVEL >= level )) {
					log(output);
				}
			}
		}
	}
	
	public static void print(String message) {
		print(message, Constants.DEFAULT_INDENT_LEVEL);
	}
	
	public static void print(String message, boolean verbose) {
		print(message, Constants.DEFAULT_INDENT_LEVEL, verbose);
	}
	
	public static void print(String message, int level) {
		print(message, level, false);
	}
	
	public static void print(String message, int level, boolean verbose) {
		if( (Constants.PRINT_LEVEL == -1 || Constants.PRINT_LEVEL >= level) && (!verbose || Constants.VERBOSE)) {
			String output = "[PRINT]";
			if( Constants.PRINT ) {
				for( int f=0; f<level*Constants.DEFAULT_INDENT_INCREMENT; f++ ) {
					output += Constants.PRINT_INDENT;
				}
				output += Constants.HEADER;
				output += message;
				consolePrint(output);
				if( Constants.LOG && ( Constants.LOG_LEVEL == -1 || Constants.LOG_LEVEL >= level )) {
					log(output);
				}
			}
		}
	}
	
	public static void log(String message) {
//		BufferedWriter logWriter;
//		try {
//			logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
//					new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ) ), Constants.ENCODING));
//			logWriter.write(message);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public static void consolePrint(String message) {
		System.out.println(message);
		for( IGuild guild : Brain.cli.getGuilds() ) {
			BotUtils.sendLog(guild, message);
		}
	}
}
