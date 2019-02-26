package caris.modular.embedbuilders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;

import caris.framework.utilities.StringUtilities;
import caris.modular.tokens.TBAMatchObject;
import caris.modular.utilities.TBAObjectFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class TBABuilder {

	public static final int PAGE_SIZE = 12;
	
	public static final String TBA_LAMP_URL = "https://lh3.googleusercontent.com/XJMfH3PCD9Vy2J5sg3d1sew4IFf2BIgtCpg921n0F2lQyMvmOhrsoY9UIqqrm_5GLw";
	public static final String TBA_LAMP_ICON_URL = "https://www.thebluealliance.com/images/tba_lamp.svg";
	
	public static EmbedBuilder TBAQueueBuilder = new EmbedBuilder().withAuthorIcon(TBA_LAMP_URL)
																	.withTitle("Match Queue")
																	.withFooterIcon(TBA_LAMP_ICON_URL)
																	.withColor(4149685);
	
	public static EmbedBuilder alertBuilder = new EmbedBuilder().withAuthorIcon(TBA_LAMP_URL)
																	.withTitle("Match Alert")
																	.withFooterIcon(TBA_LAMP_ICON_URL);
	
	public static EmbedBuilder teamCardBuilder = new EmbedBuilder().withAuthorIcon(TBA_LAMP_URL)
																	.withFooterIcon(TBA_LAMP_ICON_URL);
	
	public static EmbedObject getQueueBuilder(TBAMatchObject[] matchObjects) {
		if( matchObjects.length == 0 ) {
			return null;
		}
		TBAQueueBuilder.clearFields();
		TBAQueueBuilder.withAuthorName("The Blue Alliance - " + matchObjects[0].eventKey);
		TBAQueueBuilder.withAuthorUrl("https://www.thebluealliance.com/event/" + matchObjects[0].eventKey);
		TBAQueueBuilder.withFooterText("https://www.thebluealliance.com/api/v3/event/" + matchObjects[0].eventKey + "/matches");
		
		for( int f=0; f<Math.min(PAGE_SIZE, matchObjects.length); f++ ) {
			TBAMatchObject match = matchObjects[f];
			TBAQueueBuilder.appendField("[" + match.matchNumber + "] " + match.matchType.toString(), "```diff\n- [" + String.join(" | ", match.redAlliance) + "]\n```", true);
			TBAQueueBuilder.appendField(match.getDayOfWeek() + " | " + match.getDate() + " | " + match.getTime(), "```ini\n- [" + String.join(" | ", match.blueAlliance) + "]\n```", true);
		}
		
		return TBAQueueBuilder.build();
	}
	
	public static EmbedObject getAlertEmbed(TBAMatchObject matchObject, String team) {
		alertBuilder.clearFields();
		alertBuilder.withAuthorName("Match Starting Soon!");
		alertBuilder.withTitle(matchObject.matchType.toString() + " [" + matchObject.matchNumber + "] - " + matchObject.getTime());
		if( StringUtilities.containsIgnoreCase(matchObject.redAlliance, team) ) {
			alertBuilder.appendField("Red Alliance", "```diff\n- [" + String.join(" | ", matchObject.redAlliance) + "]\n```", false);
			alertBuilder.appendField("Blue Alliance", "```ini\n- [" + String.join(" | ", matchObject.blueAlliance) + "]\n```", false);
			alertBuilder.withColor(Color.RED);
		} else if( StringUtilities.containsIgnoreCase(matchObject.blueAlliance, team) ) {
			alertBuilder.appendField("Blue Alliance", "```ini\n- [" + String.join(" | ", matchObject.blueAlliance) + "]\n```", false);
			alertBuilder.appendField("Red Alliance", "```diff\n- [" + String.join(" | ", matchObject.redAlliance) + "]\n```", false);
			alertBuilder.withColor(Color.BLUE);
		} else {
			alertBuilder.appendField("Red Alliance", "```diff\n- [" + String.join(" | ", matchObject.redAlliance) + "]\n```", false);
			alertBuilder.appendField("Blue Alliance", "```ini\n- [" + String.join(" | ", matchObject.blueAlliance) + "]\n```", false);
			alertBuilder.withColor(255, 0, 255);
		}
		alertBuilder.withFooterText("Event - " + matchObject.eventKey);
		return alertBuilder.build();
	}
	
	public static EmbedObject[] paginate(JSONArray queueArray) {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		TBAMatchObject[] sortedMatches = TBAObjectFactory.generateTBAMatchQueue(queueArray);
		if( sortedMatches == null ) {
			return null;
		}
		List<TBAMatchObject> sortedMatchList = Arrays.asList(sortedMatches);
		List<TBAMatchObject> contents = new ArrayList<TBAMatchObject>();
		for( int f=0; f<sortedMatchList.size(); f++ ) {
			contents.add(sortedMatchList.get(f));
			if( contents.size() == PAGE_SIZE ) {
				TBAMatchObject[] page = new TBAMatchObject[PAGE_SIZE];
				for( int g=0; g<PAGE_SIZE; g++ ) {
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
	
	public static EmbedObject[] paginate(JSONArray queueArray, String teamFilter) {
		List<EmbedObject> pages = new ArrayList<EmbedObject>();
		TBAMatchObject[] sortedMatches = TBAObjectFactory.generateTBAMatchQueue(queueArray, teamFilter);
		if( sortedMatches == null ) {
			return null;
		}
		List<TBAMatchObject> sortedMatchList = Arrays.asList(sortedMatches);
		List<TBAMatchObject> contents = new ArrayList<TBAMatchObject>();
		for( int f=0; f<sortedMatchList.size(); f++ ) {
			contents.add(sortedMatchList.get(f));
			if( contents.size() == PAGE_SIZE ) {
				TBAMatchObject[] page = new TBAMatchObject[PAGE_SIZE];
				for( int g=0; g<PAGE_SIZE; g++ ) {
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
