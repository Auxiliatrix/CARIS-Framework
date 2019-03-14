package caris.framework.embedbuilders;

import java.awt.Color;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

public class ModulesBuilder {

	public static EmbedBuilder modulesBuilder = new EmbedBuilder()
														.withColor(Color.BLUE)
														.withFooterIcon(Brain.cli.getApplicationIconURL())
														.withFooterText(Constants.INVOCATION_PREFIX + "Modules (enable | disable) <module>");

	public static EmbedObject getModulesEmbed(IGuild guild) {
		modulesBuilder.clearFields();
		modulesBuilder.withAuthorIcon(guild.getIconURL());
		modulesBuilder.withAuthorName(guild.getName());
		String disabled = "";
		String enabled = "";
		for( Handler h : Brain.modules.values() ) {
			if( h.isRoot() ) {
				enabled += "- " + h.name + " (ROOT)\n";
			}
		}
		for( Handler h : Brain.modules.values() ) {
			if( !h.isRoot() ) {
				if( h.disabledOn(guild.getLongID()) ) {
					disabled += "- " + h.name + "\n";
				} else {
					enabled += "- " + h.name + "\n";
				}

			}
		}
		if( disabled == "" ) {
			disabled = "There are no disabled Modules for this Guild.";
		}
		if( enabled == "" ) {
			enabled = "There are no enabled Modules for this Guild.";
		}
		modulesBuilder.appendField("Disabled Modules:", disabled, false);
		modulesBuilder.appendField("Enabled Modules:", enabled, false);
		return modulesBuilder.build();
	}
	
}
