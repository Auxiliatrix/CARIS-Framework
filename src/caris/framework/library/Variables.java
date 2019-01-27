package caris.framework.library;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONException;
import org.json.JSONObject;

public class Variables implements JSONable {
	

	// Dynamic global variables
	public AtomicReference<JSONObject> atomicVariableData;
	
	/* Gigantic Variable Library */
	public ConcurrentHashMap<Long, GuildInfo> guildIndex;
	
	/* Global UserData */
	public ConcurrentHashMap<Long, GlobalUserInfo> globalUserIndex;
	
	public Variables( JSONObject json ) throws JSONReloadException {
		this();
		if( json != null ) {
			try {
				atomicVariableData = new AtomicReference<JSONObject>(json.getJSONObject("atomicVariableData"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				JSONObject JSONglobalUserIndex = json.getJSONObject("globalUserIndex");
				for( Object key : JSONglobalUserIndex.keySet() ) {
					try {
						globalUserIndex.put(Long.parseLong(key.toString()), new GlobalUserInfo(JSONglobalUserIndex.getJSONObject(key.toString())));
					} catch (JSONReloadException e){
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				JSONObject JSONguildIndex = json.getJSONObject("guildIndex");
				for( Object key : JSONguildIndex.keySet() ) {
					try {
						guildIndex.put(Long.parseLong(key.toString()), new GuildInfo(JSONguildIndex.getJSONObject(key.toString())));
					} catch (JSONReloadException e){
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			throw new JSONReloadException();
		}
	}
	
	public Variables() {
		atomicVariableData = new AtomicReference<JSONObject>(new JSONObject());
		guildIndex = new ConcurrentHashMap<Long, GuildInfo>();
		globalUserIndex = new ConcurrentHashMap<Long, GlobalUserInfo>();
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
		JSONData.put("atomicVariableData", atomicVariableData.get());
		return JSONData;
	}
}
