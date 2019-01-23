package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IMessage;

public abstract class InteractiveHandler extends Handler {

	public IMessage source;
	
	public InteractiveHandler(String name) {
		this(name, false, null);
	}
	
	public InteractiveHandler(String name, boolean allowBots) {
		this(name, allowBots, null);
	}
	
	public InteractiveHandler(String name, IMessage source) {
		this(name, false, source);
	}
	
	public InteractiveHandler(String name, boolean allowBots, IMessage source) {
		super(name);
		this.source = source;
	}

	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking interactive " + name, 0, true);
		if( event instanceof ReactionAddEvent ) {
			ReactionAddEvent reactionAddEvent = (ReactionAddEvent) event;
			if( source != null && reactionAddEvent.getMessage() == source ) {
				Logger.debug("Interaction with interactive " + name + ". Processing.", 1);
				if( botFilter(event) ) {
					Reaction result = process(reactionAddEvent);
					if( result == null ) {
						Logger.debug("No Reaction produced. Aborting.", 1, true);
					} else {
						Logger.debug("Reaction produced from interactive " + name + ". Adding to queue." , 1);
					}
					return result;
				} else {
					Logger.debug("Event from a bot. Aborting.", 1, true);
					return null;
				}
			} else {
				Logger.debug("Non-source interaction. Aborting.", 1, true);
				return null;
			}
		} else {
			Logger.debug("Not a ReactionAddEvent. Aborting.", 1, true);
			return null;
		}
	}

	public abstract Reaction process(ReactionAddEvent reactionAddEvent);
	
	@Override
	public abstract String getDescription();
	
	public void deactivate() {
		Brain.interactives.remove(this);
	}
}
