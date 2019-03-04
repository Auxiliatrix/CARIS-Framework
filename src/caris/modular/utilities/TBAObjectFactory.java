package caris.modular.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caris.framework.utilities.StringUtilities;
import caris.modular.tokens.TBAMatchObject;

public class TBAObjectFactory {

	public static TBAMatchObject[] generateTBAMatchQueue(JSONArray queueArray) {
		return generateTBAMatchQueue(queueArray, null);
	}
	
	public static TBAMatchObject[] generateTBAMatchQueue(JSONArray queueArray, String teamFilter) {
		List<TBAMatchObject> matches = new ArrayList<TBAMatchObject>();
		for( int f=0; f<queueArray.length(); f++ ) {
			try {
				TBAMatchObject match = generateTBAMatchObject(queueArray.getJSONObject(f));
				if( match != null ) {
					if( teamFilter == null ) {
						matches.add(match);
					}
					else if( StringUtilities.containsIgnoreCase(match.redAlliance, teamFilter) ) {
						matches.add(match);
					}
					else if( StringUtilities.containsIgnoreCase(match.blueAlliance, teamFilter) ) {
						matches.add(match);
					}
				}
			} catch (JSONException e) {
				return null;
			}
		}
		TBAMatchObject[] sortedMatches = matches.toArray(new TBAMatchObject[matches.size()]);
		Arrays.sort(sortedMatches);
		if( sortedMatches.length == 0 ) {
			return null;
		}
		return sortedMatches;
	}
	
	public static TBAMatchObject generateTBAMatchObject(JSONObject matchObject) {
		String eventKey = "";
		int matchNumber = -1;
		String matchType = "";
		long predictedTime = 0L;
		List<String> redAlliance = new ArrayList<String>();
		List<String> blueAlliance = new ArrayList<String>();
		try {
			eventKey = matchObject.getString("event_key");
			matchNumber = matchObject.getInt("match_number");
			matchType = matchObject.getString("comp_level");
			predictedTime = matchObject.getLong("predicted_time");
			JSONObject alliances = matchObject.getJSONObject("alliances");
			JSONObject alliances_blue = alliances.getJSONObject("blue");
			JSONArray blueTeams = alliances_blue.getJSONArray("team_keys");
			for( int f=0; f<blueTeams.length(); f++ ) {
				blueAlliance.add(blueTeams.getString(f).replace("frc", ""));
			}
			JSONObject alliances_red = alliances.getJSONObject("red");
			JSONArray redTeams = alliances_red.getJSONArray("team_keys");
			for( int f=0; f<redTeams.length(); f++ ) {
				redAlliance.add(redTeams.getString(f).replace("frc", ""));
			}
		} catch (JSONException e) {
			return null;
		}
		return new TBAMatchObject(eventKey, matchNumber, matchType, predictedTime, redAlliance.toArray(new String[redAlliance.size()]), blueAlliance.toArray(new String[blueAlliance.size()]));
	}
	
}
