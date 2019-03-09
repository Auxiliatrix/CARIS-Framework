package caris.framework.embedbuilders;

import caris.configuration.calibration.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class CreditsBuilder {

	public static EmbedBuilder creditsBuilder = new EmbedBuilder().withColor(255, 0, 255)
																	.withAuthorName(Constants.NAME)
																	.withAuthorIcon(Brain.cli.getApplicationIconURL())
																	.withAuthorUrl("https://www.github.com/Auxiliatrix/CARIS-Framework")
																	.withTitle("Developed by Alina Kim and Anthony Zanella")
																	.withDescription("Built using the [Java Discord4j Framework](https://github.com/Discord4J/Discord4J) and the [deprecated CARIS framework](https://www.github.com/InfinityPhase/CARIS)")
																	.withFooterText("FIRST Robotics Team 604 Quixilver")
																	.appendField("Alina Kim", "https://www.github.com/Auxiliatrix", false)
																	.appendField("Anthony Zanella", "https://www.github.com/InfinityPhase", false);

	public static EmbedObject getCreditsEmbed() {
		return creditsBuilder.build();
	}
	
}
