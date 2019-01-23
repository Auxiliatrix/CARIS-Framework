package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.main.Brain;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
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
		if( event instanceof ReactionEvent ) {
			ReactionEvent reactionEvent = (ReactionEvent) event;
			if( source != null && reactionEvent.getMessage() == source ) {
				Logger.debug("Interaction with interactive " + name + ". Processing.", 1);
				if( botFilter(event) ) {
					Logger.debug("Event from a bot. Aborting.", 1, true);
					return null;
				} else {
					Reaction result = process(reactionEvent);
					if( result == null ) {
						Logger.debug("No Reaction produced. Aborting.", 1, true);
					} else {
						Logger.debug("Reaction produced from interactive " + name + ". Adding to queue." , 1);
					}
					return result;
				}
			} else {
				Logger.debug("Non-source interaction. Aborting.", 1, true);
				return null;
			}
		} else {
			Logger.debug("Not a ReactionEvent. Aborting.", 1, true);
			return null;
		}
	}

	public abstract Reaction process(ReactionEvent reactionEvent);
	
	@Override
	public abstract String getDescription();
	
	@Override
	protected boolean isBot(Event event) {
		if( event instanceof ReactionEvent ) {
			if( !Constants.RESPOND_TO_BOT && ((ReactionEvent) event).getUser().isBot() ) {
				return true;
			}
		}
		return false;
	}
	
	public void deactivate() {
		Brain.interactives.remove(this);
	}
}
