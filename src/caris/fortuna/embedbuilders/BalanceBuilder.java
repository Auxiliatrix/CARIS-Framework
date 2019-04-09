package caris.fortuna.embedbuilders;

import java.awt.Color;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class BalanceBuilder {
	public static EmbedBuilder balanceBuilder = new EmbedBuilder()
														.withTitle(":dvd: Fortunes")
														.withColor(Color.YELLOW);
	
	public static EmbedObject getBalanceEmbed(String name, int balance) {
		balanceBuilder.clearFields();
		balanceBuilder.withDescription(name + " has " + balance + " Fortunes");
		return balanceBuilder.build();
	}
}
