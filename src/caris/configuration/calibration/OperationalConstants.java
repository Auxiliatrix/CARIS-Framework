package caris.configuration.calibration;

/**
 * The OperationalConstants file contains configuration values that affect the operation of the Bot. 
 * @author Squea
 *
 */
public class OperationalConstants {
	
	/**
	 * The number of milliseconds to wait before retrying in the case of a {@link java.net.SocketException}.
	 * @see caris.framework.basereactions.Reaction#run()
	 */
	public static final int RETRY_SOCKETEXCEPTION_DELAY = 1000;
	
	/**
	 * The number of retries to attempt in the case of a {@link java.net.SocketException}.
	 * @see caris.framework.basereactions.Reaction#run()
	 */
	public final static int STUBBORNNESS = 2;
}
