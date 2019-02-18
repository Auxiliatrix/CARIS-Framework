package caris.framework.embedbuilders;

import java.awt.Color;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.calibration.Constants;
import caris.framework.calibration.PermissionsString;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

public class HelpBuilder {
	
	public static EmbedBuilder helpBuilder = new EmbedBuilder()
													.withColor(Color.CYAN)
													.withAuthorIcon(Brain.cli.getApplicationIconURL())
													.withAuthorName(Constants.NAME + " Help")
													.withTitle("Categories:")
													.withFooterText("Type `" + Constants.INVOCATION_PREFIX + "Help` <category> for commands in that category.");
	public static EmbedBuilder categoryBuilder = new EmbedBuilder()
													.withColor(Color.CYAN)
													.withAuthorIcon(Brain.cli.getApplicationIconURL())
													.withAuthorName(Constants.NAME + " Help")
													.withFooterText("Type `" + Constants.INVOCATION_PREFIX + "Help` <module> for information on how to use that module.");
	public static EmbedBuilder commandBuilder = new EmbedBuilder()
													.withColor(Color.CYAN)
													.withAuthorIcon(Brain.cli.getApplicationIconURL())
													.withAuthorName(Constants.NAME + " Help");
	
	public static EmbedObject getHelpEmbed() {
		helpBuilder.clearFields();
		String description = "";
		for( MessageHandler.Access accessLevel : MessageHandler.Access.values() ) {
			if( accessLevel == MessageHandler.Access.DEVELOPER || accessLevel == MessageHandler.Access.PASSIVE ) {
				description += "🔒 " + accessLevel.toString() + "\n";
			} else {
				description += accessLevel.toString() + "\n";
			}
		}
		if( description.isEmpty() ) {
			description = "No modules were found for this category.";
		}
		helpBuilder.withDescription("```yaml\n" + description + "```");
		return helpBuilder.build();
	}
	
	public static EmbedObject getHelpEmbed(MessageHandler.Access accessLevel) {
		categoryBuilder.clearFields();
		String description = "";
		for( String name : Brain.handlers.keySet() ) {
			Handler h = Brain.handlers.get(name);
			if( h instanceof MessageHandler ) {
				MessageHandler mh = (MessageHandler) h;
				if( mh.accessLevel == accessLevel ) {
					description += name + "\n";
				}
			} else if( accessLevel == MessageHandler.Access.PASSIVE ){
				description += name + "\n";
			}
		}
		if( description.isEmpty() ) {
			description = "```css\nNo modules were found for this category.\n```";
		} else {
			description = "```yaml\n" + description + "```";
		}
		categoryBuilder.withTitle(accessLevel.toString() + " Modules:");
		categoryBuilder.withDescription(description);
		return categoryBuilder.build();
	}
	
	public static EmbedObject getHelpEmbed(Handler h) {
		commandBuilder.clearFields();
		if( h instanceof MessageHandler ) {
			MessageHandler mh = (MessageHandler) h;
			commandBuilder.appendField("`" + mh.invocation + "`", h.getDescription(), false);
			String usage = "";
			for( String example : mh.getUsage() ) {
				usage += example + "\n";
			}
			if( usage.isEmpty() ) {
				usage = "```css\nNo commands were found for this module.\n```";
			} else {
				usage = "```http\n" + usage + "```";
			}
			commandBuilder.appendField("Usage", usage, false);
			commandBuilder.withFooterText(mh.accessLevel.toString() + formatRequirements(mh.requirements));
		} else {
			commandBuilder.appendField(h.name, h.getDescription(), false);
			commandBuilder.appendField("Usage", "```ini\n[Passive]\n```", false);
			commandBuilder.withFooterText("Passive | " + Constants.NAME + " Only");
		}
		return commandBuilder.build();
	}
	
	private static String formatRequirements(Permissions[] requirements) {
		String requirementsString = " | ";
		for( Permissions requirement : requirements ) {
			requirementsString += PermissionsString.PERMISSIONS_STRING.get(requirement) + ", ";
		}
		if( requirementsString.length() > 4 ) {
			return requirementsString.substring(0, requirementsString.length()-2);
		} else {
			return "";
		}
	}
		
}
