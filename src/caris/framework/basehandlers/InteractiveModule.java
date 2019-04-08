package caris.framework.basehandlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vdurmont.emoji.Emoji;

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

public abstract class InteractiveModule {

	public String name;
	public boolean allowBots;
	
	protected IMessage source;
	public boolean completed;
	
	public InteractiveModule() {
		Interactive self = this.getClass().getAnnotation(Interactive.class);
		name = self.name();
		allowBots = self.allowBots();
		Brain.interactives.add(this);
	}

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

	protected final boolean equivalentEmojis(IReaction reaction, Emoji emoji) {
		return reaction.getEmoji().getName().equals(ReactionEmoji.of(emoji.getUnicode()).getName());
	}
	
	protected final boolean botFilter(Event event) {
		return isBot(event) && !allowBots;
	}
	
	protected final boolean isBot(Event event) {
		if( event instanceof ReactionEvent ) {
			if( ((ReactionEvent) event).getUser().isBot() ) {
				return true;
			}
		}
		return false;
	}
		
	public final Reaction create(IMessage source) {
		this.source = source;
		this.completed = false;
		return open();
	}
	
	public final Reaction destroy() {
		Brain.interactives.remove(this);
		this.completed = true;
		return close();
	}
	
	protected abstract Reaction open();
	
	protected abstract Reaction close();
	
	public abstract Reaction process(ReactionEvent reactionEvent);
	
	public abstract MessageContent getDefault();
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Interactive {
		String name();
		boolean allowBots() default false;
	}
}
