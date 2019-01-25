package caris.framework.reactions;

import com.vdurmont.emoji.Emoji;

import caris.framework.basehandlers.InteractiveHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.utilities.BotUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class InteractiveReaction extends MultiReaction {

	public IChannel channel;
	public InteractiveHandler interactive;
	
	public InteractiveReaction(IChannel channel, InteractiveHandler interactive) {
		this(channel, interactive, 1);
	}
	
	public InteractiveReaction(IChannel channel, InteractiveHandler interactive, int priority) {
		super(1);
		this.interactive = interactive;
	}
	
	@Override
	public void run() {
		IMessage source = null;
		if( interactive.getDefault().content != "" && interactive.getDefault().embed != null ) {
			source = BotUtils.sendMessage(channel, interactive.getDefault().content, interactive.getDefault().embed);
		} else if( interactive.getDefault().content == "" && interactive.getDefault().embed == null ) {
			source = BotUtils.sendMessage(channel, "```http\nLoading Interactive...\n```");
		} else if( interactive.getDefault().content.isEmpty() ) {
			source = BotUtils.sendMessage(channel, interactive.getDefault().embed);
		} else {
			source = BotUtils.sendMessage(channel, interactive.getDefault().content);
		}
		interactive.source = source;
		for( Emoji emoji : interactive.getInitialReactions() ) {
			reactions.add(new ReactAddReaction(source, emoji));
		}
		super.run();
	}
	
}
