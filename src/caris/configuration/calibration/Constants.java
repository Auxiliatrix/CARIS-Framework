package caris.configuration.calibration;

/**
 * The Constants file contains configuration values that vary per Bot.
 * @author Alina Kim
 *
 */
public class Constants {
		
	/**
	 * The prefix that all Invocations should begin with. 
	 * e.g., cHelp if the prefix is 'c' and the Module is "Help".
	 */
	public static final String INVOCATION_PREFIX = "%";	
	
	/**
	 * The name that this Bot should be referred to as.
	 * The bot will automatically set its name to this on startup.
	 */
	public static final String NAME = "FORTUNA";

	/**
	 * The Long User IDs that this Bot should consider as a "Developer."
	 * This array is referred to in specific permissions-checking functions.
	 * @see caris.framework.basehandlers.MessageHandler#developerAuthor(sx.blah.discord.handle.obj.IUser)
	 */
	public static final long[] DEVELOPER_IDS = new long[] {
			Long.parseLong("246562987651891200"),
			Long.parseLong("365715538166415362"),
	};
	
	/**
	 * The packages from which to load Modules.
	 * All classes of type {@link caris.framework.basehandlers.Handler} from these packages will be loaded on startup.
	 */
	public static final String[] MODULE_PACKAGES = new String[] {
			"caris.modular.handlers",
			"caris.fortuna.handlers",
	};
	
}
