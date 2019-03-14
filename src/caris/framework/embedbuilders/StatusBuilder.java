package caris.framework.embedbuilders;

import java.awt.Color;

import caris.configuration.calibration.Constants;
import caris.framework.main.Brain;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class StatusBuilder {

	public static EmbedBuilder statusBuilder = new EmbedBuilder()
													.withColor(Color.GREEN)
													.withAuthorName(Constants.NAME + " Status")
													.withAuthorIcon(Brain.cli.getApplicationIconURL());

	public static EmbedObject getStatusEmbed(long ping) {
		statusBuilder.clearFields();
		statusBuilder.withTitle("**ONLINE**");
		statusBuilder.appendField("Ping", "```" + StringUtilities.leftJustify(ping+"", 29) + "```", true);
		statusBuilder.appendField("Threads", "```" + StringUtilities.leftJustify(Brain.threadCount+"", 29) + "```", true);
		statusBuilder.appendField("Guilds", "```" + StringUtilities.leftJustify(Brain.cli.getGuilds().size()+"", 29) + "```", true);
		statusBuilder.appendField("Users", "```" + StringUtilities.leftJustify(Brain.cli.getUsers().size()+"", 29	) + "```", true);
		return statusBuilder.build();
	}
		
}
