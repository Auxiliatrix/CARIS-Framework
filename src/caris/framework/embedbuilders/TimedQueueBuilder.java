package caris.framework.embedbuilders;

import java.awt.Color;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class TimedQueueBuilder {

	public static EmbedBuilder timedQueueBuilder = new EmbedBuilder()
														.withColor(Color.GREEN)
														.withAuthorIcon(Brain.cli.getApplicationIconURL())
														.withAuthorName("CARIS Timed Queue")
														.withTitle("Pending Tasks: ");
	
	public static EmbedObject getTimedQueue() {
		timedQueueBuilder.clearFields();
		timedQueueBuilder.withFooterText(Brain.timedQueue.size() + " item(s) in queue");
		for( Reaction r : Brain.timedQueue.keySet() ) {
			long time = Brain.timedQueue.get(r);
			timedQueueBuilder.appendField("T-" + (System.currentTimeMillis() - time) / 1000 + " seconds", r.toString(), false);
		}
		return timedQueueBuilder.build();
	}
	
}
