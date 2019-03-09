package caris.framework.basehandlers;

import com.vdurmont.emoji.Emoji;

import caris.configuration.calibration.Constants;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.tokens.MessageContent;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;

public abstract class InteractiveHandler extends Handler {

	protected IMessage source;
	public boolean completed;
	
	public InteractiveHandler(String name) {
		this(name, false);
	}
	
	public InteractiveHandler(String name, boolean allowBots) {
		super(name, allowBots);
		this.source = null;
		Brain.interactives.add(this);
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
		} else if( event instanceof MessageDeleteEvent ) {
			MessageDeleteEvent messageDeleteEvent = (MessageDeleteEvent) event;
			if( source != null && messageDeleteEvent.getMessage() == source ) {
				Logger.debug("Interactive source was deleted. Removing interactive.");
				destroy();
				return null;
			} else {
				Logger.debug("Not a ReactionEvent. Aborting.", 1, true);
				return null;
			}
		} else {
			Logger.debug("Not a ReactionEvent. Aborting.", 1, true);
			return null;
		}
	}

	protected boolean equivalentEmojis(IReaction reaction, Emoji emoji) {
		return reaction.getEmoji().getName().equals(ReactionEmoji.of(emoji.getUnicode()).getName());
	}
	
	public Reaction create(IMessage source) {
		this.source = source;
		this.completed = false;
		return open();
	}
	
	public Reaction destroy() {
		Brain.interactives.remove(this);
		this.completed = true;
		return close();
	}
	
	public abstract Reaction process(ReactionEvent reactionEvent);
	
	protected abstract Reaction open();
	
	protected abstract Reaction close();
	
	public abstract MessageContent getDefault();
	
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
}
