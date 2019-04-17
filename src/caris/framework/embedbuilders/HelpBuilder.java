package caris.framework.embedbuilders;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.main.Brain;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
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
			category = StringUtilities.trim(category, EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 16, true);
			if( description.length() + category.length() > EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 16 ) {
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
		category = StringUtilities.trim(category, EmbedBuilder.TITLE_LENGTH_LIMIT - 9, true);
		categoryBuilder.clearFields();
		String description = "";
		for( String name : Brain.modules.keySet() ) {
			Handler h = Brain.modules.get(name);
			Help helpAnnotation = h.getClass().getAnnotation(Help.class);
			if( helpAnnotation != null ) {
				if( helpAnnotation.category().equalsIgnoreCase(category) ) {
					name = StringUtilities.trim(name, EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 32, true);
					if( description.length() + name.length() > EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 32 ) {
						categoryBuilder.withDescription("```yaml\n" + description + "```");
						categoryBuilder.withTitle(category + " Modules:");
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
			categoryBuilder.withTitle(category + " Modules:");
			pages.add(categoryBuilder.build());
		} else if( pages.isEmpty() ) {
			categoryBuilder.withDescription("```css\nNo modules were found for this category.\n```");
			categoryBuilder.withTitle(category + " Modules:");
			pages.add(categoryBuilder.build());
		}
		return pages.toArray(new EmbedObject[pages.size()]);
	}
	
	public static EmbedObject[] getHelpEmbed(Handler h, IGuild guild) {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		commandBuilder.clearFields();
		Help helpAnnotation = h.getClass().getAnnotation(Help.class);
		if( helpAnnotation != null ) {
			String footerText = StringUtilities.trim(helpAnnotation.category(), EmbedBuilder.FOOTER_CONTENT_LIMIT / 2, true) + (h instanceof MessageHandler ? ((MessageHandler) h).getFormattedRequirements() : "") + (h.disabledOn(guild.getLongID()) ? " | DISABLED" : "");
			String invocationText = h.disabledOn(guild.getLongID()) ? "~~" + StringUtilities.trim(h.invocation, EmbedBuilder.TITLE_LENGTH_LIMIT - 15, true) + "~~ (DISABLED)" : "`" + StringUtilities.trim(h.invocation, EmbedBuilder.TITLE_LENGTH_LIMIT - 2, true) + "`";
			String descriptionText = h.disabledOn(guild.getLongID()) ? StringUtilities.trim(helpAnnotation.description(), EmbedBuilder.FIELD_CONTENT_LIMIT, true) : StringUtilities.trim(helpAnnotation.description(), EmbedBuilder.FIELD_CONTENT_LIMIT, true);
			
			String usage = "";
			for( String example : helpAnnotation.usage() ) {
				example = StringUtilities.trim(example, EmbedBuilder.FIELD_CONTENT_LIMIT - 14, true);
				if( usage.length() + example.length() > EmbedBuilder.FIELD_CONTENT_LIMIT - 14 ) {
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
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Help {
		String category() default "Default";
		String description() default "An active module.";
		String[] usage() default {};
	}
	
}
