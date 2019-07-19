package caris.framework.modules;

import caris.configuration.calibration.Constants;
import caris.framework.reactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.handle.obj.Permissions;

public abstract class MessageModule<E extends MessageEvent> extends Module<E> {

	public abstract String getDescription();
	public abstract String getCategory();
	
	public boolean allowBots() {
		return true;
	}
	
	public Permissions[] getRequirements() {
		return new Permissions[0];
	}
	
	public MessageModule(Class<E> eventClass) {
		super(eventClass);
	}

	@Override
	public Reaction handle(E event) {
		Logger verboseLogger = logger.clone().setVerbose(true);
		if( triggered(event) ) {
			if( allowBots() || !isBot(event ) ) {
				if( requirementsMet(event) || developerAuthor(event) ) {
					logger.clone().setType("PROCESS").log("Success: Reaction generated");
					return process(event);
				}
				verboseLogger.log("Failure: Requirements not met");
			}
			verboseLogger.log("Failure: Bot-originated event");
		}
		verboseLogger.log("Failure: Conditions unmet");
		return null;
	}
	
	public final boolean isBot(E event) {
		return event.getAuthor().isBot();
	}
	
	public final boolean requirementsMet(E event) {
		boolean result = true;
		for( Permissions requirement : getRequirements() ) {
			result &= event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(requirement);
		}
		return result;
	}
	
	public final boolean developerAuthor(E event) {
		for( Long id : Constants.DEVELOPER_IDS ) {
			if( event.getAuthor().getLongID() == id ) {
				return true;
			}
		}
		return false;
	}
	
}
