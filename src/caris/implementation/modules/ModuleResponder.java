package caris.implementation.modules;

import caris.framework.main.Brain;
import caris.framework.modules.MessageModule;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.Reaction.Tag;
import caris.implementation.events.MessageEventWrapper;
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
	public boolean preconditionsMet(MessageReceivedEvent event) {
		MessageEventWrapper mew = new MessageEventWrapper(event);
		return mew.getQualifiedWords().containsIgnoreCase(Brain.name);
	}

	@Override
	public Reaction process(MessageReceivedEvent event) {
		MessageEventWrapper mew = new MessageEventWrapper(event);
		return new ReactionMessageSend(Tag.RECESSIVE, mew.getChannel(), "That's me!");
	}
	
}
