package caris.framework.handlers;

import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.SaveDataReaction;

public class SaveHandler extends MessageHandler {

	public SaveHandler() {
		super("Save", false, Access.DEVELOPER);
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper);
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		return new SaveDataReaction();
	}

	@Override
	public String getDescription() {
		return "Manually triggers the serialization of the global variables into .ser files.";
	}

	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(invocation, "Activates the serialization process.");
		return usage;
	}
}
