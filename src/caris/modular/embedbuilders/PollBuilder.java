package caris.modular.embedbuilders;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import caris.configuration.calibration.Constants;
import caris.framework.tokens.Duration;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class PollBuilder {
	
	public static EmbedBuilder pollBuilder = new EmbedBuilder().withColor(Color.YELLOW)
													.withDescription("Vote by reacting with the option number!\nYou can end the poll at any time by reacting with :octagonal_sign:");

	public static EmbedBuilder resultBuilder = new EmbedBuilder().withColor(Color.YELLOW);
												
	public static EmbedObject getPollEmbed(String question, String[] options, HashMap<String, Integer> votes, IUser owner, Duration timeout) {
		pollBuilder.clearFields();
		pollBuilder.withAuthorIcon(owner.getAvatarURL());
		pollBuilder.withAuthorName(StringUtilities.trim(owner.getName(), Constants.EMBED_AUTHOR_SIZE - 7, true) + "'s Poll");
		pollBuilder.withTitle(StringUtilities.trim(question, Constants.EMBED_TITLE_SIZE, true));
		for( int f=0; f<Math.min(10, options.length); f++ ) {
			pollBuilder.appendField("[" + (f+1) + "] " + StringUtilities.trim(options[f], Constants.EMBED_FIELD_TITLE_SIZE - 4, true), votes.get(options[f]) + (votes.get(options[f]) == 1 ? " vote" : " votes"), false);
		}
		if( timeout != null ) {
			pollBuilder.withFooterText("Poll will last for: " + timeout.asString());
		} else {
			pollBuilder.withFooterText("Poll will end when the creator reacts with :octagonal_sign:");
		}
		return pollBuilder.build();
	}
	
	public static EmbedObject getResultEmbed(String question, HashMap<String, Integer> votes) {
		resultBuilder.clearFields();
		resultBuilder.withAuthorName(question);
		String[] sortedVotes = votes.keySet().toArray(new String[votes.size()]);
		int total = 0;
		for( String option : votes.keySet() ) {
			total += votes.get(option);
		}
		Arrays.sort(sortedVotes, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				return votes.get(arg1) - votes.get(arg0);
			}
		});
		total = Math.max(total, 1);
		resultBuilder.withTitle(StringUtilities.trim(sortedVotes[0], Constants.EMBED_TITLE_SIZE - 26, true) + " [" + votes.get(sortedVotes[0]) + " " + (votes.get(sortedVotes[0]) == 1 ? "vote" : "votes") + " | " + (votes.get(sortedVotes[0]) * 100) / total + "%]");
		String content = "";	
		for( String option : sortedVotes ) {
			content += votes.get(option) + " " + (votes.get(option) == 1 ? "vote" : "votes") + " for " + StringUtilities.trim(option, 180, true) + "\n";
		}
		resultBuilder.withDescription("```http\n" + content + "```");
		return resultBuilder.build();
	}

}
