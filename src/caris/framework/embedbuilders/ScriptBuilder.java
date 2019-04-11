package caris.framework.embedbuilders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import caris.configuration.calibration.Constants;
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
		scriptListBuilder.withAuthorName(StringUtilities.trim(guild.getName(), Constants.EMBED_AUTHOR_SIZE - 8, true) + " Scripts");
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		String description = "";
		try {
			for( Object key : Brain.variables.atomicVariableData.get().getJSONObject("scripts").keySet() ) {
				String name = StringUtilities.trim((String) key, Constants.EMBED_DESCRIPTION_SIZE - 23, true);
				if( description.length() + name.length() > Constants.EMBED_DESCRIPTION_SIZE - 23 ) {
					scriptListBuilder.withDescription("```yaml\n" + description + "\n```");
					pages.add(scriptListBuilder.build());
					scriptListBuilder.clearFields();
					description = "";
				}
				description += name + " [Global]";
			}
		} catch (JSONException e) {
			Logger.error(e.getMessage());
		}
		try {
			for( Object key : Brain.variables.getGuildInfo(guild).guildData.getJSONObject("scripts").keySet() ) {
				String name = StringUtilities.trim((String) key, Constants.EMBED_DESCRIPTION_SIZE - 14, true);
				if( description.length() + name.length() > Constants.EMBED_DESCRIPTION_SIZE - 14 ) {
					scriptListBuilder.withDescription("```yaml\n" + description + "\n```");
					pages.add(scriptListBuilder.build());
					scriptListBuilder.clearFields();
					description = "";
				}
				description += name;
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
		scriptBuilder.withDescription("```" + StringUtilities.trim(source, Constants.EMBED_DESCRIPTION_SIZE, true) + "```");
		scriptBuilder.withFooterText(requirements);
		return scriptBuilder.build();
	}
	
}
