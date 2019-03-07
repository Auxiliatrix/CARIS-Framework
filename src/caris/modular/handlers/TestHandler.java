package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;

public class TestHandler extends MessageHandler {

	public TestHandler() {
		super("Test", "Developer");
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper) && messageEventWrapper.developerAuthor;
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		return null;
	}

	@Override
	public String getDescription() {
		return "A test module that does different things depending on what's being debugged. You're not supposed to use it.";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add(invocation);
		return usage;
	}
}
