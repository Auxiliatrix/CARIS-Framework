package caris.modular.embedbuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import caris.modular.tokens.TBAMatchObject;
import caris.modular.utilities.TBAMatchObjectFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class TBABuilder {

	public static final String TBA_LAMP_URL = "https://lh3.googleusercontent.com/XJMfH3PCD9Vy2J5sg3d1sew4IFf2BIgtCpg921n0F2lQyMvmOhrsoY9UIqqrm_5GLw";
	public static final String TBA_LAMP_ICON_URL = "https://www.thebluealliance.com/images/tba_lamp.svg";
	
	public static EmbedBuilder TBAQueueBuilder = new EmbedBuilder().withAuthorIcon(TBA_LAMP_URL)
																	.withTitle("Match Queue")
																	.withFooterIcon(TBA_LAMP_ICON_URL)
																	.withColor(4149685);
	
	public static EmbedObject getQueueBuilder(TBAMatchObject[] TBAMatchObjects) {
		if( TBAMatchObjects.length == 0 ) {
			return null;
		}
		TBAQueueBuilder.clearFields();
		TBAQueueBuilder.withAuthorName("The Blue Alliance - " + TBAMatchObjects[0].eventKey);
		TBAQueueBuilder.withAuthorUrl("https://www.thebluealliance.com/event/" + TBAMatchObjects[0].eventKey);
		TBAQueueBuilder.withFooterText("https://www.thebluealliance.com/api/v3/event/" + TBAMatchObjects[0].eventKey + "/matches");
		
		for( int f=0; f<Math.min(12, TBAMatchObjects.length); f++ ) {
			TBAMatchObject match = TBAMatchObjects[f];
			TBAQueueBuilder.appendField("[" + match.matchNumber + "] " + match.matchType.toString(), "```diff\n- [" + String.join(" | ", match.redAlliance) + "]\n```", true);
			TBAQueueBuilder.appendField(match.getDayOfWeek() + " | " + match.getDate() + " | " + match.getTime(), "```ini\n- [" + String.join(" | ", match.blueAlliance) + "]\n```", true);
		}
		
		return TBAQueueBuilder.build();
	}
	
	public static EmbedObject[] paginate(JSONArray queueArray) {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		List<TBAMatchObject> matches = new ArrayList<TBAMatchObject>();
		for( int f=0; f<queueArray.length(); f++ ) {
			try {
				TBAMatchObject match = TBAMatchObjectFactory.generateTBAMatchObject(queueArray.getJSONObject(f));
				if( match != null ) {
					matches.add(match);
				}
			} catch (JSONException e) {
				return null;
			}
		}
		TBAMatchObject[] sortedMatches = matches.toArray(new TBAMatchObject[matches.size()]);
		Arrays.sort(sortedMatches);
		List<TBAMatchObject> sortedMatchList = Arrays.asList(sortedMatches);
		List<TBAMatchObject> contents = new ArrayList<TBAMatchObject>();
		for( int f=0; f<sortedMatchList.size(); f++ ) {
			contents.add(sortedMatchList.get(f));
			if( contents.size() == 12 ) {
				TBAMatchObject[] page = new TBAMatchObject[12];
				for( int g=0; g<12; g++ ) {
					page[g] = contents.get(g);
				}
				contents = new ArrayList<TBAMatchObject>();
				pages.add(getQueueBuilder(page));
			}
		}
		if( contents.size() > 0 ) {
			TBAMatchObject[] page = new TBAMatchObject[contents.size()];
			for( int f=0; f<contents.size(); f++ ) {
				page[f] = contents.get(f);
			}
			contents = new ArrayList<TBAMatchObject>();
			pages.add(getQueueBuilder(page));
		}
		if( pages.size() == 0 ) {
			return null;
		}
		return pages.toArray(new EmbedObject[pages.size()]);
	}
	
}
