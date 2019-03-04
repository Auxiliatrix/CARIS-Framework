package caris.modular.interactives;

import caris.framework.basehandlers.InteractiveHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.basereactions.ReactionRunnable;
import caris.framework.calibration.EmojiSet;
import caris.framework.main.Brain;
import caris.framework.reactions.InteractiveDestroyReaction;
import caris.framework.reactions.ReactAddReaction;
import caris.framework.reactions.ReactClearReaction;
import caris.framework.tokens.MessageContent;
import caris.modular.reactions.TBAMatchAlertReaction;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;

public class TBAMatchAlertInteractive extends InteractiveHandler {

	public String mentioned;
	public EmbedObject defaultEmbed;
	public int id;
	
	public TBAMatchAlertInteractive(String mentions, EmbedObject embed, int id) {
		super("TBAMatchAlert");
		this.mentioned = mentions;
		this.defaultEmbed = embed;
		this.id = id;
	}
	
	@Override
	public Reaction process(ReactionEvent reactionEvent) {
		MultiReaction interact = new MultiReaction();
		if( !(reactionEvent instanceof ReactionAddEvent) ) {
			return null;
		}
		MultiReaction interaction = new MultiReaction(-1);
		if( equivalentEmojis(reactionEvent.getReaction(), EmojiSet.STOP ) ) {
			interact.add(new ReactionRunnable(new Runnable() {
				@Override
				public void run() {
					for( Reaction r : Brain.timedQueue.keySet() ) {
						if( r instanceof TBAMatchAlertReaction ) {
							TBAMatchAlertReaction alert = (TBAMatchAlertReaction) r;
							if( alert.id == id ) {
								Brain.timedQueue.remove(r);
							}
						}
					}
				}
			}));
			for( InteractiveHandler interactive : Brain.interactives ) {
				if( interactive instanceof TBAMatchAlertInteractive ) {
					TBAMatchAlertInteractive alertInteractive = (TBAMatchAlertInteractive) interactive;
					if( alertInteractive.id == id ) {
						interact.add(new InteractiveDestroyReaction(interactive));
					}
				}
			}
		}
		return interaction;
	}
	
	@Override
	public MessageContent getDefault() {
		return new MessageContent(mentioned, defaultEmbed);
	}
	
	@Override
	public String getDescription() {
		return "React with :stop: to stop notifications";
	}

	@Override
	protected Reaction open() {
		return new ReactAddReaction(source, EmojiSet.STOP);
	}

	@Override
	protected Reaction close() {
		return new ReactClearReaction(source);
	}
	
}
