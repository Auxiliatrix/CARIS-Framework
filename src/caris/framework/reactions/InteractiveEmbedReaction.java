package caris.framework.reactions;

import caris.framework.basehandlers.InteractiveHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class InteractiveEmbedReaction extends Reaction {

	public InteractiveHandler interactive;
	public EmbedObject embed;
	public IChannel channel;
	
	public InteractiveEmbedReaction(InteractiveHandler interactive, EmbedObject embed, IChannel channel) {
		this(interactive, embed, channel, 2);
	}
	
	public InteractiveEmbedReaction(InteractiveHandler interactive, EmbedObject embed, IChannel channel, int priority) {
		super(priority);
		this.interactive = interactive;
		this.embed = embed;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Logger.say(embed.toString(), channel);
		IMessage message = BotUtils.sendMessage(channel, embed);
		interactive.source = message;
		Brain.interactives.add(interactive);
		
	}
}
