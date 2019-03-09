package caris.framework.basehandlers;

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
			MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
			if( !messageReceivedEvent.getChannel().isPrivate() ) {
				MessageEventWrapper messageEventWrapper = wrap(messageReceivedEvent);
				inContext = false;
				if( Brain.variables.getUserInfo(messageReceivedEvent.getMessage()).userData.has("lastMessage_" + messageReceivedEvent.getChannel().getLongID()) ) {
					if( StringUtilities.containsIgnoreCase(Brain.variables.getUserInfo(messageReceivedEvent.getMessage()).userData.get("lastMessage_" + messageReceivedEvent.getChannel().getLongID()).toString(), Constants.NAME) ) {
						inContext = true;
					}
				}
				if( botFilter(event) ) {
					Logger.debug("Event from a bot. Aborting.", 1, true);
					return null;
				} else if( isTriggered(messageEventWrapper) && accessGranted(messageEventWrapper) ) {
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
	
	private MessageEventWrapper wrap(MessageReceivedEvent messageReceivedEvent) {
		MessageEventWrapper messageEventWrapper = new MessageEventWrapper(messageReceivedEvent);
		if( messageEventWrapper.tokens.size() > 0 ) {
			String token = messageEventWrapper.tokens.get(0);
			if( token.startsWith("{") && token.endsWith("}") && token.length() > 2 && messageEventWrapper.message.length() > token.length() + 1) {
				String tokenContent = token.substring(1, token.length()-1);
				try {
					Long channelID = Long.parseLong(tokenContent);
					for( IGuild guild : Brain.cli.getGuilds() ) {
						for( IChannel channel : guild.getChannels() ) {
							if( channel.getLongID() == channelID ) {
								messageEventWrapper = new MessageEventWrapper(
														new MessageReceivedEvent(
															new RedirectedMessage(messageReceivedEvent.getMessage(), channel, messageEventWrapper.message.substring(messageEventWrapper.message.indexOf("}"+2)))
														));
							}
						}
					}
				} catch (NumberFormatException e) {
					
				}
			}
		}
		return messageEventWrapper;
	}
	
	protected boolean mentioned(MessageEventWrapper messageEventWrapper) {
		return messageEventWrapper.containsWord(Constants.NAME) || inContext;
	}
	
	protected boolean invoked(MessageEventWrapper messageEventWrapper) {
		return messageEventWrapper.tokens.size() > 0 ? messageEventWrapper.tokens.get(0).equalsIgnoreCase(invocation) : false;
	}
	
	protected boolean accessGranted(MessageEventWrapper messageEventWrapper) {
		boolean meetsRequirements = true;
		for( Permissions requirement : requirements ) {
			meetsRequirements &= messageEventWrapper.getAuthor().getPermissionsForGuild(messageEventWrapper.getGuild()).contains(requirement);
		}
		return meetsRequirements || messageEventWrapper.developerAuthor;
	}
	
	protected int getBotPosition(MessageEventWrapper messageEventWrapper) {
		return getPosition(messageEventWrapper, Brain.cli.getOurUser());
	}
	
	protected int getPosition(MessageEventWrapper messageEventWrapper, IUser user) {
		int maxPosition = -1;
		for( IRole role : user.getRolesForGuild(messageEventWrapper.getGuild()) ) {
			if( role.getPosition() > maxPosition ) {
				maxPosition = role.getPosition();
			}
		}
		return maxPosition;
	}
		
	protected abstract boolean isTriggered(MessageEventWrapper messageEventWrapper);
	protected abstract Reaction process(MessageEventWrapper messageEventWrapper);

}
