package caris.implementation.modules;

import caris.framework.main.Brain;
import caris.framework.modules.MessageModule;
import caris.framework.reactions.Reaction;
import caris.implementation.reactions.ReactionMessageSend;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class ModuleResponder extends MessageModule<MessageReceivedEvent> {

	@Override
	public String getName() {
		return "Responder";
	}
	
	@Override
	public String getDescription() {
		return "Responds when this bot's name is said.";
	}

	@Override
	public String getCategory() {
		return "Default";
	}
	
	public ModuleResponder() {
		super(MessageReceivedEvent.class);
	}
	
	@Override
	public boolean triggered(MessageReceivedEvent event) {
		return event.getMessage().getContent().contains(Brain.name);
	}

	@Override
	public Reaction process(MessageReceivedEvent event) {
		return new ReactionMessageSend(event.getChannel(), "That's me!");
	}
	
}