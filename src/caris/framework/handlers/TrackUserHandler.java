package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.TrackUserReaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;

public class TrackUserHandler extends GeneralHandler<UserJoinEvent> {

	public TrackUserHandler() {
		super("TrackUser");
	}
	
	@Override
	protected boolean isTriggered(UserJoinEvent typedEvent) {
		return true;
	}
	
	@Override
	protected Reaction process(UserJoinEvent typedEvent) {
		Logger.print("New user " + typedEvent.getUser().getLongID() + " joined (" + typedEvent.getGuild().getLongID() + ") <" + typedEvent.getGuild().getName() + ">", 0);
		MultiReaction welcome = new MultiReaction(-1);
		welcome.add(new TrackUserReaction(typedEvent.getGuild(), typedEvent.getUser()));
		welcome.add(new MessageReaction(Brain.variables.getGuildInfo(typedEvent.getGuild()).getDefaultChannel(), ("Welcome, " + typedEvent.getUser().getName() + "!")));
		return welcome;
	}
	
	@Override
	public String getDescription() {
		return "Handles the joining of new users on " + Constants.NAME + "'s servers.";
	}
}
