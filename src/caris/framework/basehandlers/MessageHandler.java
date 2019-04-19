package caris.framework.basehandlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import caris.configuration.calibration.Constants;
import caris.configuration.reference.PermissionsString;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.tokens.RedirectedMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

/**
 * The MessageHandler is an extension of the {@link caris.framework.basehandlers.Handler} class.
 * It is designed to specifically handle {@link sx.blah.discord.api.events.MessageReceivedEvent} objects.
 * It serves as a template class, and provides functions that can be used to process Permissions and invocation types.
 * 
 * @author Alina Kim
 *
 */
public abstract class MessageHandler extends Handler {
	
	protected Permissions[] requirements;
	
	public MessageHandler() {
		this(new Permissions[] {});
	}
	
	public MessageHandler(Permissions...requirements) {
		super();
		
		this.requirements = requirements;
	}
	
	protected MessageHandler(String name, boolean allowBots, boolean whitelist, boolean root) {
		this(name, allowBots, whitelist, root, new Permissions[] {});
	}
	
	protected MessageHandler(String name, boolean allowBots, boolean whitelist, boolean root, Permissions...requirements) {
		super(name, allowBots, whitelist, root);
		
		this.requirements = requirements;
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		if( event instanceof MessageReceivedEvent ) {
			MessageReceivedEvent mre = (MessageReceivedEvent) event;
			if( !mre.getChannel().isPrivate() ) {
				MessageEventWrapper messageEventWrapper = wrap(mre);
				if( botFilter(event) ) {
					Logger.debug("Event from a bot. Aborting.", 1, true);
					return null;
				}
				if( disableFilter(event) ) {
					Logger.debug("Handler disabled for this location. Aborting.", 1, true);
					return null;
				}
				if( isTriggered(messageEventWrapper) && accessGranted(messageEventWrapper) ) {
					Logger.debug("Conditions satisfied for " + name + ". Processing.", 1);
					Reaction result = process(messageEventWrapper);
					if( result == null ) {
						Logger.debug("No Reaction produced. Aborting.", 1, true);
					} else {
						Logger.debug("Reaction produced from " + name + ". Adding to queue." , 1);
					}
					return result;
				} else {
					Logger.debug("Conditions unsatisfied. Aborting.", 1, true);
					return null;
				}
			} else {
				Logger.debug("Message from a private channel. Aborting.", 1, true);
				return null;
			}
		} else {
			Logger.debug("Event not a MessageReceivedEvent. Aborting.", 1, true);
			return null;
		}
	}
	
	public static final int getBotPosition(IGuild guild) {
		return getPosition(guild, Brain.cli.getOurUser());
	}
	
	public static final int getPosition(IGuild guild, IUser user) {
		int maxPosition = -1;
		for( IRole role : user.getRolesForGuild(guild) ) {
			if( role.getPosition() > maxPosition ) {
				maxPosition = role.getPosition();
			}
		}
		return maxPosition;
	}
	
	public static final boolean developerAuthor(IUser user) {
		for( Long id : Constants.DEVELOPER_IDS ) {
			if( user.getLongID() == id ) {
				return true;
			}
		}
		return false;
	}
	
	protected final boolean accessGranted(MessageEventWrapper mew) {
		boolean meetsRequirements = true;
		for( Permissions requirement : requirements ) {
			meetsRequirements &= mew.getAuthor().getPermissionsForGuild(mew.getGuild()).contains(requirement);
		}
		return meetsRequirements || developerAuthor(mew.getAuthor());
	}
	
	protected final boolean mentioned(MessageEventWrapper mew) {
		return mew.containsWord(Constants.NAME) || mew.getAllMentionedUsers().contains(Brain.cli.getOurUser()) || inContext(mew);
	}
	
	protected final boolean inContext(MessageEventWrapper mew) {
		if( mew.getMessage().getMentions().contains(Brain.cli.getOurUser()) || Brain.variables.getUserInfo(mew.getMessage()).userData.has("lastMessage_" + mew.getChannel().getLongID()) ) {
			if( StringUtilities.containsIgnoreCase(Brain.variables.getUserInfo(mew.getMessage()).userData.get("lastMessage_" + mew.getChannel().getLongID()).toString(), Constants.NAME) ) {
				return true;
			}
		}
		return false;
	}
	
	protected final boolean invoked(MessageEventWrapper mew) {
		return mew.tokens.size() > 0 ? StringUtilities.equalsAnyOfIgnoreCase(mew.tokens.get(0), aliases) : false;
	}
	
	private final MessageEventWrapper wrap(MessageReceivedEvent mre) {
		MessageEventWrapper mew = new MessageEventWrapper(mre);
		if( mew.tokens.size() > 0 ) {
			String token = mew.tokens.get(0);
			if( token.startsWith("{") && token.endsWith("}") && token.length() > 2 && mew.message.length() > token.length() + 1) {
				String tokenContent = token.substring(1, token.length()-1);
				try {
					Long channelID = Long.parseLong(tokenContent);
					for( IGuild guild : Brain.cli.getGuilds() ) {
						for( IChannel channel : guild.getChannels() ) {
							if( channel.getLongID() == channelID ) {
								mew = new MessageEventWrapper(
														new MessageReceivedEvent(
															new RedirectedMessage(mre.getMessage(), channel, mew.message.substring(mew.message.indexOf("}"+2)))
														));
							}
						}
					}
				} catch (NumberFormatException e) {
					
				}
			}
		}
		return mew;
	}
	
	public String getFormattedRequirements() {
		String requirementsString = " | ";
		for( Permissions requirement : requirements ) {
			requirementsString += PermissionsString.PERMISSIONS_STRING.get(requirement) + ", ";
		}
		if( requirementsString.length() > 4 ) {
			return requirementsString.substring(0, requirementsString.length()-2);
		} else {
			return "";
		}
	}
	
	protected abstract boolean isTriggered(MessageEventWrapper mew);
	protected abstract Reaction process(MessageEventWrapper mew);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Command {
		String[] aliases() default {};
	}
	
}
