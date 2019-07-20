package caris.framework.reactions;

import sx.blah.discord.handle.obj.IChannel;

public class ReactionMessageSend extends Reaction {
	
	private IChannel channel;
	private String message;
	
	public ReactionMessageSend(IChannel channel, String message) {
		this.channel = channel;
		this.message = message;
	}
	
	@Override
	protected void task() {
		channel.sendMessage(message);
	}
	
}
