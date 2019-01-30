package caris.modular.embedbuilders;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import caris.framework.tokens.Duration;
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
		pollBuilder.withAuthorName("Poll [created by " + owner.getName() + "]");
		pollBuilder.withTitle(question);
		for( int f=0; f<Math.min(10, options.length); f++ ) {
			pollBuilder.appendField("[" + f + "] " + options[f], votes.get(options[f]) + " votes", false);
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
		resultBuilder.withTitle(sortedVotes[0] + " [" + votes.get(sortedVotes[0]) + " | " + votes.get(sortedVotes[0]) * 100 / total + "]");
		String description = "";
		for( String option : sortedVotes ) {
			description += option + " [" + votes.get(option) + " | " + votes.get(option) * 100 / total + "]\n";
		}
		resultBuilder.withDescription("```HTTP\n" + description + "```");
		return resultBuilder.build();
	}

}
