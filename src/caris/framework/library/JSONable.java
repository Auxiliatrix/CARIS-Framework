package caris.framework.library;

import org.json.JSONObject;

public interface JSONable {
	
	public JSONObject getJSONData();
	
	class JSONReloadException extends Exception {
		public JSONReloadException() {
			super();
		}
		
		public JSONReloadException(String message) {
			super(message);
		}
	}
	
}
