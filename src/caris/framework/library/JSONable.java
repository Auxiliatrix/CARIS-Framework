package caris.framework.library;

import java.util.HashMap;

import org.json.JSONObject;

public interface JSONable {
	public JSONObject getJSONData();
	
	public default JSONObject JSONify(HashMap<String, Object> data) {
		HashMap<String, JSONObject> JSONData = new HashMap<String, JSONObject>();
		for( Object key : data.keySet() ) {
			if( data.get(key) instanceof JSONable ) {
				JSONData.put(key.toString(), ((JSONable) data.get(key)).getJSONData());
			} else {
				JSONData.put(key.toString(), new JSONObject(data.get(key)));
			}
		}
		return new JSONObject(JSONData);
	}
}
