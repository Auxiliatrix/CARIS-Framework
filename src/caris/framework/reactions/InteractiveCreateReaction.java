package caris.framework.reactions;

import caris.framework.basehandlers.InteractiveModule;
import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class InteractiveCreateReaction extends Reaction {

	public IChannel channel;
	public InteractiveModule interactive;
	
	public InteractiveCreateReaction(IChannel channel, InteractiveModule interactive) {
		this(channel, interactive, -1);
	}
	
	public InteractiveCreateReaction(IChannel channel, InteractiveModule interactive, int priority) {
		super(1);
		this.channel = channel;
		this.interactive = interactive;
	}
	
	@Override
	public void process() {
		IMessage source = null;
		if( !interactive.getDefault().content.isEmpty() && interactive.getDefault().embed != null ) {
			source = channel.sendMessage(interactive.getDefault().content, interactive.getDefault().embed);
		} else if( interactive.getDefault().content.isEmpty() && interactive.getDefault().embed == null ) {
			source = channel.sendMessage("```http\nLoading Interactive...\n```");
		} else if( interactive.getDefault().content.isEmpty() ) {
			source = channel.sendMessage(interactive.getDefault().embed);
		} else {
			source = channel.sendMessage(interactive.getDefault().content);
		}
		interactive.create(source).start();
	}
	
}
