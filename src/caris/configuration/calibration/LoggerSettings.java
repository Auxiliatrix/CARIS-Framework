package caris.configuration.calibration;

/**
 * The LoggerSettings file contains settings that affect how and what events are displayed in the log.
 * @author Alina Kim
 *
 */
public class LoggerSettings {

	/* Display Settings */
	
	/**
	 * The character to indent Print logs with.
	 */
	public final static char PRINT_INDENT = '=';
	
	/**
	 * The character to indent Debug logs with.
	 */
	public final static char DEBUG_INDENT = '.';
	
	/**
	 * The character to indent Error logs with.
	 */
	public final static char ERROR_INDENT = '!';
	
	/**
	 * The character to head each log with.
	 */
	public final static String HEADER = "> ";
	
	/**
	 * The default level of indentation.
	 */
	public final static int DEFAULT_INDENT_LEVEL = 0;
	
	/**
	 * The default number of characters to increase by per indent level.
	 */
	public final static int DEFAULT_INDENT_INCREMENT = 2;
	
	
	/* Verbosity Settings */
	
	/**
	 * Whether to display logs classified as Verbose.
	 */
	public static final boolean VERBOSE = false;
	
	
	/**
	 * Whether to display Debug logs.
	 */
	public static final boolean DEBUG = true;
	
	/**
	 * Whether to display Print events.
	 */
	public static final boolean PRINT = true;
	
	/**
	 * Whether to display Log events.
	 */
	public static final boolean LOG = true;
	
	/**
	 * Whether to display Say logs.
	 */
	public static final boolean SAY = true;
	
	/**
	 * Whether to display Hear logs.
	 */
	public static final boolean HEAR = true;
	
	/**
	 * The maximum level of indentation to display for Debug events.
	 * Higher levels of indentation will be considered non-important.
	 * Set to -1 to include all levels of indentation.
	 */
	public static final int DEBUG_LEVEL = 2;
	
	/**
	 * The maximum level of indentation to display for Print events.
	 * Higher levels of indentation will be considered non-important.
	 * Set to -1 to include all levels of indentation.
	 */
	public static final int PRINT_LEVEL = -1;
	
	/**
	 * The maximum level of indentation to display for Log events.
	 * Higher levels of indentation will be considered non-important.
	 * Set to -1 to include all levels of indentation.
	 */
	public static final int LOG_LEVEL = -1;
	
}
