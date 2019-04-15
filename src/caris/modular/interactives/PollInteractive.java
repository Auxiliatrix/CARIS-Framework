package caris.modular.interactives;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.vdurmont.emoji.Emoji;

import caris.configuration.reference.EmojiSet;
import caris.framework.basehandlers.InteractiveModule;
import caris.framework.basehandlers.InteractiveModule.Interactive;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.reactions.InteractiveDestroyReaction;
import caris.framework.reactions.MessageEditReaction;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.ReactAddReaction;
import caris.framework.reactions.ReactClearReaction;
import caris.framework.reactions.SetTimedReaction;
import caris.framework.tokens.Duration;
import caris.framework.tokens.MessageContent;
import caris.modular.embedbuilders.PollBuilder;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.obj.IUser;

@Interactive(name = "Poll")
public class PollInteractive extends InteractiveModule {
	
	public String question;
	public HashMap<String, Integer> votes;
	public String[] options;
	public IUser owner;
	public Duration timeout;
	
	public PollInteractive(String question, String[] options) {
		this(question, options, Brain.cli.getOurUser(), null);
	}
	
	public PollInteractive(String question, String[] options, IUser owner) {
		this(question, options, owner, null);
	}
	
	public PollInteractive(String question, String[] options, Duration timeout) {
		this(question, options, Brain.cli.getOurUser(), timeout);
	}
	
	public PollInteractive(String question, String[] options, IUser owner, Duration timeout) {
		super();
		this.question = question;
		this.options = options;
		votes = new HashMap<String, Integer>();
		for( String option : options ) {
			votes.put(option, 0);
		}
		this.owner = owner;
		this.timeout = timeout;
	}

	@Override
	public Reaction process(ReactionEvent reactionEvent) {
		if( reactionEvent instanceof ReactionAddEvent ) {
			if( equivalentEmojis(reactionEvent.getReaction(), EmojiSet.STOP) && reactionEvent.getUser().equals(owner) ) {
				return new InteractiveDestroyReaction(this);	
			}
			for( int f=1; f<Math.min(10, options.length)+1; f++ ) {
				if( equivalentEmojis(reactionEvent.getReaction(), EmojiSet.NUMBERS[f]) ) {
					votes.put(options[f-1], votes.get(options[f-1])+1);
					return new MessageEditReaction(source, new MessageContent("", PollBuilder.getPollEmbed(question, options, votes, owner, timeout)));
				}
			}
		} else {
			for( int f=1; f<Math.min(10, options.length)+1; f++ ) {
				if( equivalentEmojis(reactionEvent.getReaction(), EmojiSet.NUMBERS[f]) ) {
					votes.put(options[f-1], votes.get(options[f-1])-1);
					return new MessageEditReaction(source, new MessageContent("", PollBuilder.getPollEmbed(question, options, votes, owner, timeout)));
				}
			}
		}
		return null;
	}

	@Override
	protected Reaction open() {
		completed = false;
		MultiReaction openPoll = new MultiReaction(-1);
		List<Emoji> emojis = new ArrayList<Emoji>();
		emojis.addAll(Arrays.asList(EmojiSet.NUMBERS).subList(1, Math.min(10, options.length)+1));
		emojis.add(EmojiSet.STOP);
		for( Emoji emoji : emojis ) {
			openPoll.add(new ReactAddReaction(source, emoji));
		}
		if( timeout != null ) {
			openPoll.add(new SetTimedReaction(new InteractiveDestroyReaction(this), timeout, source.getTimestamp().atZone(ZoneId.systemDefault()).toEpochSecond()*1000));
		}
		return openPoll;
	}
	
	@Override
	protected Reaction close() {
		MultiReaction closePoll = new MultiReaction();
		closePoll.add(new MessageReaction(source.getChannel(), PollBuilder.getResultEmbed(question, votes)));
		closePoll.add(new ReactClearReaction(source));
		completed = true;
		return closePoll;
	}
	
	@Override
	public MessageContent getDefault() {
		return new MessageContent("", PollBuilder.getPollEmbed(question, options, votes, owner, timeout));
	}
	
}
