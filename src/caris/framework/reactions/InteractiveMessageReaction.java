package caris.framework.reactions;

import caris.framework.basehandlers.InteractiveHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class InteractiveMessageReaction extends Reaction {

	public InteractiveHandler interactive;
	public String content;
	public IChannel channel;
	
	public InteractiveMessageReaction( InteractiveHandler interactive, String content, IChannel channel ) {
		this(interactive, content, channel, 1);
	}
	
	public InteractiveMessageReaction( InteractiveHandler interactive, String content, IChannel channel, int priority ) {
		super(priority);
		this.interactive = interactive;
		this.content = content;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Logger.say(content, channel);
		IMessage message = BotUtils.sendMessage(channel, content);
		interactive.source = message;
		Brain.interactives.add(interactive);
	}
	
}
