package caris.framework.embedbuilders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import caris.framework.main.Brain;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

public class ScriptBuilder {
	
	public static EmbedBuilder scriptListBuilder = new EmbedBuilder()
														.withColor(Color.CYAN);
	
	public static EmbedBuilder scriptBuilder = new EmbedBuilder()
													.withAuthorIcon(Brain.cli.getApplicationIconURL())
													.withAuthorName(Brain.cli.getApplicationName())
													.withColor(Color.CYAN);
	
	public static EmbedObject[] getScriptListEmbed(IGuild guild) {
		scriptListBuilder.clearFields();
		scriptListBuilder.withAuthorIcon(guild.getIconURL());
		scriptListBuilder.withAuthorName(StringUtilities.trim(guild.getName(), EmbedBuilder.AUTHOR_NAME_LIMIT - 8, true) + " Scripts");
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		String description = "";
		try {
			for( Object key : Brain.variables.atomicVariableData.get().getJSONObject("scripts").keySet() ) {
				String name = StringUtilities.trim((String) key, EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 32, true);
				if( description.length() + name.length() > EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 32 ) {
					scriptListBuilder.withDescription("```yaml\n" + description + "\n```");
					pages.add(scriptListBuilder.build());
					scriptListBuilder.clearFields();
					description = "";
				}
				description += name + " [Global]\n";
			}
		} catch (JSONException e) {
			Logger.error(e.getMessage());
		}
		try {
			for( Object key : Brain.variables.getGuildInfo(guild).guildData.getJSONObject("scripts").keySet() ) {
				String name = StringUtilities.trim((String) key, EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 16, true);
				if( description.length() + name.length() > EmbedBuilder.DESCRIPTION_CONTENT_LIMIT - 16 ) {
					scriptListBuilder.withDescription("```yaml\n" + description + "\n```");
					pages.add(scriptListBuilder.build());
					scriptListBuilder.clearFields();
					description = "";
				}
				description += name + "\n";
			}
		} catch (JSONException e) {
			Logger.error(e.getMessage());
		}
		if( !description.isEmpty() ) {
			scriptListBuilder.withDescription("```yaml\n" + description + "```");
			pages.add(scriptListBuilder.build());
		} else if( pages.isEmpty() ) {
			scriptListBuilder.withDescription("```yaml\nNo custom scripts exist.```");
			pages.add(scriptListBuilder.build());
		}
		return pages.toArray(new EmbedObject[pages.size()]);
	}
	
	public static EmbedObject getScriptEmbed(String name, String source, IGuild guild, String requirements) {
		scriptBuilder.clearFields();
		scriptBuilder.withTitle(name + "[" + (guild == null ? "Global" : guild.getName()) + "]");
		scriptBuilder.withDescription("```" + StringUtilities.trim(source, EmbedBuilder.DESCRIPTION_CONTENT_LIMIT, true) + "```");
		scriptBuilder.withFooterText(requirements);
		return scriptBuilder.build();
	}
	
}
