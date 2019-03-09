package caris.framework.embedbuilders;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import caris.configuration.calibration.Constants;
import caris.configuration.reference.PermissionsString;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
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
		for( String category : Handler.categories ) {
			description += category.toString() + "\n";
		}
		if( description.isEmpty() ) {
			description = "No modules were found for this category.";
		}
		helpBuilder.withDescription("```yaml\n" + description + "```");
		return helpBuilder.build();
	}
	
	public static EmbedObject getHelpEmbed(String category) {
		categoryBuilder.clearFields();
		String description = "";
		for( String name : Brain.modules.keySet() ) {
			Handler h = Brain.modules.get(name);
			Help activeAnnotation = h.getClass().getAnnotation(Help.class);
			if( activeAnnotation != null ) {
				if( activeAnnotation.category().equalsIgnoreCase(category) ) {
					description += name + "\n";
				}
			}
		}
		if( description.isEmpty() ) {
			description = "```css\nNo modules were found for this category.\n```";
		} else {
			description = "```yaml\n" + description + "```";
		}
		categoryBuilder.withTitle(category.toString() + " Modules:");
		categoryBuilder.withDescription(description);
		return categoryBuilder.build();
	}
	
	public static EmbedObject getHelpEmbed(Handler h) {
		commandBuilder.clearFields();
		Help activeAnnotation = h.getClass().getAnnotation(Help.class);
		if( activeAnnotation != null ) {
			commandBuilder.appendField("`" + h.invocation + "`", activeAnnotation.description(), false);
			String usage = "";
			for( String example : activeAnnotation.usage() ) {
				usage += example + "\n";
			}
			if( usage.isEmpty() ) {
				usage = "```css\nNo commands were found for this module.\n```";
			} else {
				usage = "```http\n" + usage + "```";
			}
			commandBuilder.appendField("Usage", usage, false);
			commandBuilder.withFooterText(activeAnnotation.category() + (h instanceof MessageHandler ? formatRequirements(((MessageHandler) h).requirements) : ""));
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
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Help {
		String category() default "Default";
		String description() default "An active module.";
		String[] usage() default {};
	}
	
}
