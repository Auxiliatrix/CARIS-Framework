package caris.framework.library;

import java.util.HashMap;

import org.json.JSONObject;

public class Variables implements JSONable {
	

	// Dynamic global variables
	public JSONObject variableData;
	
	/* Gigantic Variable Library */
	public HashMap<Long, GuildInfo> guildIndex;
	
	/* Global UserData */
	public HashMap<Long, GlobalUserInfo> globalUserIndex;
	
	public Variables() {
		variableData = new JSONObject();
		guildIndex = new HashMap<Long, GuildInfo>();
		globalUserIndex = new HashMap<Long, GlobalUserInfo>();
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONObject JSONguildIndex = new JSONObject();
		JSONObject JSONglobalUserIndex = new JSONObject();
		for( Long key : guildIndex.keySet() ) {
			JSONguildIndex.put(key.toString(), guildIndex.get(key).getJSONData());
		}
		for( Long key : globalUserIndex.keySet() ) {
			JSONglobalUserIndex.put(key.toString(), globalUserIndex.get(key).getJSONData());
		}
		JSONData.put("guildIndex", JSONguildIndex);
		JSONData.put("globalUserIndex", JSONglobalUserIndex);
		JSONData.put("variableData", variableData);
		return JSONData;
	}
}
