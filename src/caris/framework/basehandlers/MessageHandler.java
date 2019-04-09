package caris.framework.basehandlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import caris.configuration.calibration.Constants;
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

public abstract class MessageHandler extends Handler {
		
	public Permissions[] requirements;
	public boolean inContext;
	
	public MessageHandler() {
		this(new Permissions[] {});
	}
	
	public MessageHandler(Permissions...requirements) {
		super();
		
		this.requirements = requirements;
		this.inContext = false;
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		if( event instanceof MessageReceivedEvent ) {
			MessageReceivedEvent mre = (MessageReceivedEvent) event;
			if( !mre.getChannel().isPrivate() ) {
				MessageEventWrapper messageEventWrapper = wrap(mre);
				inContext = false;
				if( mre.getMessage().getMentions().contains(Brain.cli.getOurUser()) || Brain.variables.getUserInfo(mre.getMessage()).userData.has("lastMessage_" + mre.getChannel().getLongID()) ) {
					if( StringUtilities.containsIgnoreCase(Brain.variables.getUserInfo(mre.getMessage()).userData.get("lastMessage_" + mre.getChannel().getLongID()).toString(), Constants.NAME) ) {
						inContext = true;
					}
				}
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
	
	protected final boolean accessGranted(MessageEventWrapper mew) {
		boolean meetsRequirements = true;
		for( Permissions requirement : requirements ) {
			meetsRequirements &= mew.getAuthor().getPermissionsForGuild(mew.getGuild()).contains(requirement);
		}
		return meetsRequirements || mew.developerAuthor;
	}
	
	protected final int getBotPosition(MessageEventWrapper mew) {
		return getPosition(mew, Brain.cli.getOurUser());
	}
	
	protected final int getPosition(MessageEventWrapper mew, IUser user) {
		int maxPosition = -1;
		for( IRole role : user.getRolesForGuild(mew.getGuild()) ) {
			if( role.getPosition() > maxPosition ) {
				maxPosition = role.getPosition();
			}
		}
		return maxPosition;
	}
	
	protected final boolean mentioned(MessageEventWrapper mew) {
		return mew.containsWord(Constants.NAME) || mew.getAllMentionedUsers().contains(Brain.cli.getOurUser()) || inContext;
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
	
	protected abstract boolean isTriggered(MessageEventWrapper mew);
	protected abstract Reaction process(MessageEventWrapper mew);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Command {
		String[] aliases() default {};
	}
	
}
