package caris.framework.modules;

import caris.framework.main.Brain;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessageSend;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class ModuleResponder extends Module<MessageReceivedEvent> {

	@Override
	public String getName() {
		return "Responder";
	}
	
	public ModuleResponder() {
		super(MessageReceivedEvent.class);
	}
	
	@Override
	public boolean triggered(MessageReceivedEvent event) {
		return event.getMessage().getContent().contains(Brain.name);
	}

	@Override
	public Reaction handle(MessageReceivedEvent event) {
		return new ReactionMessageSend(event.getChannel(), "That's me!");
	}
}
