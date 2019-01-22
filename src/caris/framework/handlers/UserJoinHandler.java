package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionUserJoin;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;

public class UserJoinHandler extends GeneralHandler {

	public UserJoinHandler() {
		super("UserJoin");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof UserJoinEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		UserJoinEvent userJoinEvent = (UserJoinEvent) event;
		Logger.print("New user " + userJoinEvent.getUser().getLongID() + " joined (" + userJoinEvent.getGuild().getLongID() + ") <" + userJoinEvent.getGuild().getName() + ">", 0);
		MultiReaction welcome = new MultiReaction(-1);
		welcome.reactions.add(new ReactionUserJoin(userJoinEvent.getGuild(), userJoinEvent.getUser()));
		String addedRoles = "";
		if( !welcome.reactions.isEmpty() && addedRoles.length() > 2) {
			addedRoles = addedRoles.substring(0, addedRoles.length()-2);
			welcome.reactions.add(new ReactionMessage(("Welcome, " + userJoinEvent.getUser().getName() + "!" +  
					"\nYou have been given the following roles: "+ addedRoles + "!"), Variables.guildIndex.get(userJoinEvent.getGuild()).getDefaultChannel()));
		} else {
			welcome.reactions.add(new ReactionMessage(("Welcome, " + userJoinEvent.getUser().getName() + "!"), Variables.guildIndex.get(userJoinEvent.getGuild()).getDefaultChannel()));
		}
		return welcome;
	}
	
	@Override
	public String getDescription() {
		return "Handles the joining of new users on " + Constants.NAME + "'s servers.";
	}
}
