package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

/**
 * The General Handler class is an extension of the {@link caris.framework.basehandlers.Handler} class.
 * It is type-parameterized for an extension of the {@link sx.blah.discord.api.events.Event} class, which it filters for automatically when handling.
 * It serves as a template for Modules that handle Events other than the MessageReceivedEvent.
 * 
 * @author Alina Kim
 *
 * @param <T> Extension of the {@link sx.blah.discord.api.events.Event} class
 */
public abstract class GeneralHandler<T extends Event> extends Handler {
	
	/**
	 * {@inheritDoc}
	 */
	public GeneralHandler() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected GeneralHandler(String name, boolean allowBots, boolean whitelist, boolean root) {
		super(name, allowBots, whitelist, root);
	}
	
	/**
	 * The GeneralHandler handle method automatically filters for Bots, disabling, and Event type before processing.
	 * @param event Event to process
	 * @return Reaction Actions to execute as a result of the processing
	 */
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		@SuppressWarnings("unchecked")
		T typedEvent = (T) event;
		
		/* Filter irrelevant events */
		if( botFilter(event) ) {
			Logger.debug("Event from a bot. Aborting.", 1, true);
			return null;
		}
		if( disableFilter(event) ) {
			Logger.debug("Handler disabled for this location. Aborting.", 1, true);
			return null;
		}
		
		/* Process Event */
		try {
			if( isTriggered(typedEvent) ) { // Casting is expected to break here for logging purposes
				Logger.debug("Conditions satisfied for " + name + ". Processing.", 1);
				Reaction result = process(typedEvent);
				if( result == null ) {
					Logger.debug("No Reaction produced. Aborting", 1, true);
				} else {
					Logger.debug("Reaction produced from " + name + ". Adding to queue." , 1);
				}
				return result;
			} else { // Abort if trigger conditions are not met
				Logger.debug("Conditions unsatisfied. Aborting.", 1, true);
				return null;
			}
		} catch (ClassCastException e) {
			Logger.debug("Event mismatch. Aborting.", 1, true);
			return null;
		}
	}
	
	/**
	 * This method should return the result of the checks that must be met in order for the Event to be considered relevant.
	 * @param typedEvent Event to check
	 * @return boolean Whether the trigger conditions are met
	 */
	protected abstract boolean isTriggered(T typedEvent);
	
	/**
	 * This method takes a relevant Event and generates an appropriate Reaction in response.
	 * @param typedEvent Event to process
	 * @return Reaction Actions to execute as a result of the processing
	 */
	protected abstract Reaction process(T typedEvent);
	
}
