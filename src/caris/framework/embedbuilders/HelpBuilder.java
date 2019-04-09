package caris.framework.embedbuilders;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
import caris.configuration.reference.PermissionsString;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.main.Brain;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
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
	
	public static EmbedObject[] getHelpEmbed() {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		helpBuilder.clearFields();
		String description = "";
		for( String category : Handler.categories ) {
			category = StringUtilities.trim(category, Constants.EMBED_DESCRIPTION_SIZE - 12, true);
			if( description.length() + category.length() > Constants.EMBED_DESCRIPTION_SIZE - 12 ) {
				helpBuilder.withDescription("```yaml\n" + description + "```");
				pages.add(helpBuilder.build());
				helpBuilder.clearFields();
				description = "";
			}
			description += category + "\n";
		}
		if( !description.isEmpty() ) {
			helpBuilder.withDescription("```yaml\n" + description + "```");
			pages.add(helpBuilder.build());
		} else if( pages.isEmpty() ) {
			helpBuilder.withDescription("```yaml\nNo modules were found for this category.```");
			pages.add(helpBuilder.build());
		}
		return pages.toArray(new EmbedObject[pages.size()]);
	}
	
	public static EmbedObject[] getHelpEmbed(String category, IGuild guild) {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		categoryBuilder.clearFields();
		String description = "";
		for( String name : Brain.modules.keySet() ) {
			Handler h = Brain.modules.get(name);
			Help helpAnnotation = h.getClass().getAnnotation(Help.class);
			if( helpAnnotation != null ) {
				if( helpAnnotation.category().equalsIgnoreCase(category) ) {
					name = StringUtilities.trim(name, Constants.EMBED_DESCRIPTION_SIZE - 26, true);
					if( description.length() + name.length() > Constants.EMBED_DESCRIPTION_SIZE - 12 ) {
						categoryBuilder.withDescription("```yaml\n" + description + "```");
						categoryBuilder.withTitle(category.toString() + " Modules:");
						pages.add(categoryBuilder.build());
						categoryBuilder.clearFields();
						description = "";
					}
					if( h.disabledOn(guild.getLongID()) ) {
						description += name + " (DISABLED) \n";
					} else {
						description += name + "\n";
					}
				}
			}
		}
		if( !description.isEmpty() ) {
			categoryBuilder.withDescription("```yaml\n" + description + "```");
			categoryBuilder.withTitle(category.toString() + " Modules:");
			pages.add(categoryBuilder.build());
		} else if( pages.isEmpty() ) {
			categoryBuilder.withDescription("```css\nNo modules were found for this category.\n```");
			categoryBuilder.withTitle(category.toString() + " Modules:");
			pages.add(categoryBuilder.build());
		}
		return pages.toArray(new EmbedObject[pages.size()]);
	}
	
	public static EmbedObject[] getHelpEmbed(Handler h, IGuild guild) {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		commandBuilder.clearFields();
		Help helpAnnotation = h.getClass().getAnnotation(Help.class);
		if( helpAnnotation != null ) {
			String footerText = helpAnnotation.category() + (h instanceof MessageHandler ? formatRequirements(((MessageHandler) h).requirements) : "") + (h.disabledOn(guild.getLongID()) ? " | DISABLED" : "");
			String invocationText = h.disabledOn(guild.getLongID()) ? "~~" + StringUtilities.trim(h.invocation, Constants.EMBED_FIELD_TITLE_SIZE - 15, true) + "~~ (DISABLED)" : "`" + StringUtilities.trim(h.invocation, Constants.EMBED_FIELD_TITLE_SIZE - 2, true) + "`";
			String descriptionText = h.disabledOn(guild.getLongID()) ? StringUtilities.trim(helpAnnotation.description(), Constants.EMBED_FIELD_VALUE_SIZE, true) : StringUtilities.trim(helpAnnotation.description(), Constants.EMBED_FIELD_VALUE_SIZE, true);
			
			String usage = "";
			for( String example : helpAnnotation.usage() ) {
				example = StringUtilities.trim(example, Constants.EMBED_FIELD_VALUE_SIZE - 14, true);
				if( usage.length() + example.length() > Constants.EMBED_FIELD_VALUE_SIZE - 14 ) {
					commandBuilder.appendField(invocationText, descriptionText, false);
					usage = "```http\n" + usage + "```";
					commandBuilder.appendField("Usage", usage, false);
					commandBuilder.withFooterText(footerText);
					pages.add(commandBuilder.build());
					commandBuilder.clearFields();
					usage = "";
				}
				usage += example + "\n";
			}
			commandBuilder.appendField(invocationText, descriptionText, false);
			commandBuilder.withFooterText(footerText);
			if( !usage.isEmpty() ) {
				commandBuilder.appendField("Usage", "```http\n" + usage + "```", false);
				pages.add(commandBuilder.build());
			} else if( pages.isEmpty() ) {
				commandBuilder.appendField("Usage", "```css\nNo commands were found for this module.\n```", false);
				pages.add(commandBuilder.build());
			}
		}
		return pages.toArray(new EmbedObject[pages.size()]);
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
