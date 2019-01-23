package caris.framework.library;

import java.util.HashMap;

import org.json.JSONObject;

public class Variables implements JSONable {
	
	public Variables() {}
	
	// Dynamic global variables
	public HashMap<String, Object> variableData = new HashMap<String, Object>();
	
	/* Gigantic Variable Library */
	public HashMap<Long, GuildInfo> guildIndex = new HashMap<Long, GuildInfo>();
	
	/* Global UserData */
	public HashMap<Long, GlobalUserInfo> globalUserIndex = new HashMap<Long, GlobalUserInfo>();

	@Override
	public JSONObject getJSONData() {
		HashMap<String, JSONObject> JSONData = new HashMap<String, JSONObject>();
		HashMap<String, JSONObject> JSONguildIndex = new HashMap<String, JSONObject>();
		for( Long key : guildIndex.keySet() ) {
			JSONguildIndex.put(key.toString(), guildIndex.get(key).getJSONData());
		}
		HashMap<String, JSONObject> JSONglobalUserIndex = new HashMap<String, JSONObject>();
		for( Long key : globalUserIndex.keySet() ) {
			JSONglobalUserIndex.put(key.toString(), globalUserIndex.get(key).getJSONData());
		}
		JSONData.put("guildIndex", new JSONObject(JSONguildIndex));
		JSONData.put("globalUserIndex", new JSONObject(JSONglobalUserIndex));
		JSONData.put("variableData", JSONify(variableData));
		return new JSONObject(JSONData);
	}
}
