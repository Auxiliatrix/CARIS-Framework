package caris.modular.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
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
		returnGreeting.add(new MessageReaction(messageEventWrapper.getChannel(), getRandomGreeting() + ", " + messageEventWrapper.getAuthor().getDisplayName(messageEventWrapper.getGuild()) + "!", 0));
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
	
	@Override
	public String getDescription() {
		return "Makes " + Constants.NAME + " say hi back to you!";
	}
	
	@Override
	public ArrayList<String> getUsage() {
		ArrayList<String> usage = new ArrayList<String>();
		usage.add("Hello " + Constants.NAME + "!");
		return usage;
	}
	
}
