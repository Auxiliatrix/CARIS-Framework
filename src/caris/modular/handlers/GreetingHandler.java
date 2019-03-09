package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;

public class GreetingHandler extends MessageHandler {
	
	private String[] greetingsInput = new String[] {
			"Hello",
			"Hi ",
			"Hi,",
			"Howdy",
			"G'day",
			"Gday",
			"Good morn",
			"Good even",
			"Good aftern",
			"Good day",
			"Morning",
			"Evening",
			"Afternoon",
			"Hey",
			"Yo ",
			"Yo,",
			"Sup ",
			"Sup,",
			"'Sup",
			"Salutations",
			"Greetings",
			"Hiya",
			"Hi-ya",
			"What's up",
			"Whats up"
	};
	
	private String[] greetingsOutput = new String[] {
			"Hello",
			"Hi",
			"Hey",
			"Salutations",
			"Greetings",
			"Hiya",
	};
	
	private String[] promptOutput = new String[] {
			"Yes?",
			"How may I help you?",
			"What can I do for you?",
			"Mmhm?",
			"What do you need?",
	};
	
	public GreetingHandler() {
		super("Greeting");
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return startsWithAGreeting(messageEventWrapper.message) && mentioned(messageEventWrapper);
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction returnGreeting = new MultiReaction(0);
		if( messageEventWrapper.message.endsWith("?") ) {
			returnGreeting.add(new MessageReaction(messageEventWrapper.getChannel(), getRandomPrompt(), 0));
		} else {
			returnGreeting.add(new MessageReaction(messageEventWrapper.getChannel(), getRandomGreeting() + ", " + messageEventWrapper.getAuthor().getDisplayName(messageEventWrapper.getGuild()) + "!", 0));
		}
		return returnGreeting;
	}
	
	private boolean startsWithAGreeting(String message) {
		for( String greeting : greetingsInput ) {
			if( message.toLowerCase().startsWith(greeting.toLowerCase()) ) {
				return true;
			}
		}
		return false;
	}
	
	private String getRandomGreeting() {
		return (greetingsOutput.length > 0) ? greetingsOutput[(int) (Math.random()*greetingsOutput.length)] : "Hello";
	}
	
	private String getRandomPrompt() {
		return (promptOutput.length > 0) ? promptOutput[(int) (Math.random()*promptOutput.length)] : "Yes?";
	}
	
	@Override
	public String getDescription() {
		return "Makes " + Constants.NAME + " say hi back to you!";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add("Hello " + Constants.NAME + "!");
		return usage;
	}
	
}
