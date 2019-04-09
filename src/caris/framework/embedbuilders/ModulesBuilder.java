package caris.framework.embedbuilders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.main.Brain;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

public class ModulesBuilder {

	public static EmbedBuilder modulesBuilder = new EmbedBuilder()
														.withColor(Color.BLUE)
														.withFooterIcon(Brain.cli.getApplicationIconURL())
														.withFooterText(Constants.INVOCATION_PREFIX + "Modules (enable | disable) <module>");

	public static EmbedObject[] getModulesEmbed(IGuild guild) {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		modulesBuilder.clearFields();
		String disabled = "";
		for( Handler h : Brain.modules.values() ) {
			if( !h.isRoot() ) {
				if( h.disabledOn(guild.getLongID()) ) {
					String name = StringUtilities.trim(h.name, Constants.EMBED_DESCRIPTION_SIZE - 2, true);
					if( disabled.length() + name.length() > Constants.EMBED_DESCRIPTION_SIZE - 2 ) {
						modulesBuilder.withAuthorIcon(guild.getIconURL());
						modulesBuilder.withAuthorName(guild.getName());
						modulesBuilder.appendField("Disabled Modules:", disabled, false);
						pages.add(modulesBuilder.build());
						modulesBuilder.clearFields();
						disabled = "";
					}
					disabled += "- " + h.name + "\n";
				}

			}
		}
		modulesBuilder.withAuthorIcon(guild.getIconURL());
		modulesBuilder.withAuthorName(guild.getName());
		if( !disabled.isEmpty() ) {
			modulesBuilder.appendField("Disabled Modules:", disabled, false);
			pages.add(modulesBuilder.build());
		} else if( pages.isEmpty() ) {
			modulesBuilder.appendField("Disabled Modules:", "There are no disabled Modules for this Guild.", false);
			pages.add(modulesBuilder.build());
		}
		return pages.toArray(new EmbedObject[pages.size()]);
	}
	
}
