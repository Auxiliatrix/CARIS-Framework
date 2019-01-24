package caris.framework.embedbuilders;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.calibration.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class HelpBuilder {
	
	public static EmbedBuilder helpBuilder = new EmbedBuilder()
													.withAuthorIcon(Brain.cli.getApplicationIconURL())
													.withAuthorName(Constants.NAME + " Help")
													.withDescription(Constants.NAME + " employs modules that are controlled by both commands and conversational context.")
													.withFooterIcon("Type `" + Constants.INVOCATION_PREFIX + "Help` <module> for information on how to use that module.");
	public static EmbedBuilder commandBuilder = new EmbedBuilder()
													.withAuthorIcon(Brain.cli.getApplicationIconURL())
													.withAuthorName(Constants.NAME + " Help");
	
	public static EmbedObject getHelpEmbed() {
		helpBuilder.clearFields();
		for( String name : Brain.handlers.keySet() ) {
			helpBuilder.appendField(name, Brain.handlers.get(name).getDescription(), false);
		}
		return helpBuilder.build();
	}
	
	public static EmbedObject getHelpEmbed(Handler h) {
		commandBuilder.clearFields();
		commandBuilder.withDescription(h.getDescription());
		if( h instanceof MessageHandler ) {
			MessageHandler mh = (MessageHandler) h;
			for( String example : mh.getUsage().keySet() ) {
				commandBuilder.appendField("`" + example + "`", mh.getUsage().get(example), false);
			}
			switch (mh.accessLevel) {
				case DEFAULT:
					commandBuilder.withFooterText("Active | Default");
					break;
				case ADMIN:
					commandBuilder.withFooterText("Active | Admin Only");
					break;
				case DEVELOPER:
					commandBuilder.withFooterText("Active | Developer Only");
					break;
			}
		} else {
			commandBuilder.withFooterText("Passive | " + Constants.NAME + " Only");
		}
		return commandBuilder.build();
	}
		
}
