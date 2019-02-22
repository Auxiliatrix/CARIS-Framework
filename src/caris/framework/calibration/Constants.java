package caris.framework.calibration;

public class Constants {
	public static final String INVOCATION_PREFIX = "c";	
	public static final String NAME = "CARIS";

	public static final boolean OFFLINE = false;
	public static final boolean RESPOND_TO_BOT = false; // If the user is a bot, ignore.

	public static final long[] ADMIN_IDS = new long[]{
			Long.parseLong("246562987651891200"),
			Long.parseLong("365715538166415362"),
	};
	
	public static final int MAX_MESSAGE_HISTORY = 10000;
	
	public static final int REACTION_EXECUTE_DELAY = 250;
	
	public static enum Access {
		DEFAULT {
			@Override
			public String toString() {
				return "Default";
			}
		},
		ADMIN {
			@Override
			public String toString() {
				return "Admin";
			}
		},
		DEVELOPER {
			@Override
			public String toString() {
				return "Developer";
			}
		},
		PASSIVE {
			@Override
			public String toString() {
				return "Passive";
			}
		},
	};
	
	// Debug Constants
	public static final boolean DEBUG = true;
	public static final boolean PRINT = true;
	public static final boolean LOG = true;
	public static final boolean SAY = true;
	public static final boolean HEAR = true;
	
	public static final boolean VERBOSE = false;
	
	public static final int DEBUG_LEVEL = 2;
	public static final int PRINT_LEVEL = -1;
	public static final int LOG_LEVEL = -1;

	// Simple Logger Constants
	public final static String PRINT_INDENT = "=";
	public final static String DEBUG_INDENT = ".";
	public final static String ERROR_INDENT = "!";
	public final static String HEADER = "> ";
	public final static int DEFAULT_INDENT_LEVEL = 0;
	public final static int DEFAULT_INDENT_INCREMENT = 2;

	// Execution Constants
	public final static int STUBBORNNESS = 2;
	
	// Default Off Modules
	public static final String[] DEFAULT_DISBABLED = new String[] {
			
	};
	
	public static final String COMMAND_SEPERATOR = ":";
}
