package caris.framework.embedbuilders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import caris.configuration.calibration.Constants;
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
	
	public static EmbedObject[] getTimedQueue() {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		timedQueueBuilder.clearFields();
		String footerText = Brain.timedQueue.size() + " item(s) in queue";
		timedQueueBuilder.withFooterText(footerText);
		for( Reaction r : Brain.timedQueue.keySet() ) {
			if( timedQueueBuilder.getFieldCount() >= Constants.EMBED_FIELDS_SIZE ) {
				timedQueueBuilder.withFooterText(footerText);
				pages.add(timedQueueBuilder.build());
				timedQueueBuilder.clearFields();
			}
			long time = Brain.timedQueue.get(r);
			timedQueueBuilder.appendField("T" + (System.currentTimeMillis() - time) / 1000 + " seconds", r.toString(), false);
		}
		timedQueueBuilder.withFooterText(footerText);
		if( timedQueueBuilder.getFieldCount() > 0 ) {
			pages.add(timedQueueBuilder.build());
		} else if( pages.isEmpty() ) {
			timedQueueBuilder.withDescription("Timed Queue is empty!");
			pages.add(timedQueueBuilder.build());
		}
		return pages.toArray(new EmbedObject[pages.size()]);
	}
	
}
