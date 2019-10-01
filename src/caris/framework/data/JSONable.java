package caris.framework.data;

import org.json.JSONObject;

public interface JSONable {
	
	public JSONObject getJSONData();
	
	@SuppressWarnings("serial")
	class JSONReloadException extends Exception {
		public JSONReloadException() {
			super();
		}
		
		public JSONReloadException(String message) {
			super(message);
		}
	}
	
}
