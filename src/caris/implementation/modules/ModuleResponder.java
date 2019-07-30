package caris.implementation.modules;

import caris.framework.main.Brain;
import caris.framework.modules.MessageModule;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.Reaction.Tag;
import caris.implementation.events.MessageEventWrapper;
import caris.implementation.reactions.ReactionMessageSend;

public class ModuleResponder extends MessageModule<MessageEventWrapper> {

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
		super(MessageEventWrapper.class);
	}
	
	@Override
	public boolean preconditionsMet(MessageEventWrapper event) {
		return event.getQualifiedWords().containsIgnoreCase(Brain.name);
	}

	@Override
	public Reaction process(MessageEventWrapper event) {
		return new ReactionMessageSend(Tag.RECESSIVE, event.getChannel(), "That's me!");
	}
	
}
