package caris.framework.embedbuilders;

import java.awt.Color;

import caris.framework.calibration.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class StatusBuilder {

	public static EmbedBuilder statusBuilder = new EmbedBuilder()
													.withColor(Color.GREEN)
													.withAuthorName(Constants.NAME + " Status")
													.withAuthorIcon(Brain.cli.getApplicationIconURL());

	public static EmbedObject getStatusEmbed(long ping, int threads) {
		statusBuilder.clearFields();
		statusBuilder.withTitle("**ONLINE**");
		statusBuilder.appendField("Ping", "`" + ping + "`", true);
		statusBuilder.appendField("Threads", "`" + threads + "`", true);
		return statusBuilder.build();
	}
	
	
}
