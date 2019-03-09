package caris.framework.interactives;

import caris.configuration.library.EmojiSet;
import caris.framework.basehandlers.InteractiveModule;
import caris.framework.basehandlers.InteractiveModule.Interactive;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.MessageEditReaction;
import caris.framework.reactions.ReactAddReaction;
import caris.framework.reactions.ReactClearReaction;
import caris.framework.reactions.ReactRemoveReaction;
import caris.framework.tokens.MessageContent;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;

@Interactive(name = "Paged")
public class PagedInteractive extends InteractiveModule {

	public int page;
	public EmbedObject[] pages;
	
	public PagedInteractive(EmbedObject...pages) {
		super();
		page = 0;
		this.pages = pages;
	}

	@Override
	public Reaction process(ReactionEvent reactionEvent) {
		if( !(reactionEvent instanceof ReactionAddEvent) ) {
			return null;
		}
		MultiReaction interaction = new MultiReaction(-1);
		if( equivalentEmojis(reactionEvent.getReaction(), EmojiSet.LEFT ) ) {
			page -= 1;
			while( page < 0 ) {
				page += pages.length;
			}
		} else if( equivalentEmojis(reactionEvent.getReaction(), EmojiSet.RIGHT) ) {
			page += 1;
			while( page >= pages.length ) {
				page -= pages.length;
			}
		}
		interaction.add(new ReactRemoveReaction(source, reactionEvent.getUser(), reactionEvent.getReaction()));
		interaction.add(new MessageEditReaction(source, new MessageContent("Page " + (page+1) + " of " + pages.length, pages[page])));
		return interaction;
	}

	@Override
	public MessageContent getDefault() {
		return new MessageContent("Page 1 / " + pages.length, pages[0]);
	}

	@Override
	protected Reaction open() {
		return new ReactAddReaction(source, EmojiSet.LEFT, EmojiSet.RIGHT);
	}

	@Override
	protected Reaction close() {
		return new ReactClearReaction(source);
	}
	
}
