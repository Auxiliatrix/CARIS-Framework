package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.TrackUserReaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;

public class TrackUserHandler extends GeneralHandler {

	public TrackUserHandler() {
		super("TrackUser");
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
		welcome.add(new TrackUserReaction(userJoinEvent.getGuild(), userJoinEvent.getUser()));
		welcome.add(new MessageReaction(Brain.variables.guildIndex.get(userJoinEvent.getGuild().getLongID()).getDefaultChannel(), ("Welcome, " + userJoinEvent.getUser().getName() + "!")));
		return welcome;
	}
	
	@Override
	public String getDescription() {
		return "Handles the joining of new users on " + Constants.NAME + "'s servers.";
	}
}
