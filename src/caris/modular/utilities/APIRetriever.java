package caris.modular.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import caris.framework.utilities.Logger;

public class APIRetriever {

	public static JSONObject getJSONObject(String query) {
		GetRequest request = Unirest.get(query);
		HttpResponse<JsonNode> jsonNode;
		try {
			jsonNode = request.asJson();
		} catch( UnirestException e ) {
			Logger.error("Failed to reach endpoint");
			return null;
		}
		JSONObject jsonObject = jsonNode.getBody().getObject();
		return jsonObject;
	}
	
	public static JSONArray getJSONArray(String query) {
		GetRequest request = Unirest.get(query);
		HttpResponse<JsonNode> jsonNode;
		try {
			jsonNode = request.asJson();
		} catch( UnirestException e ) {
			Logger.error("Failed to reach endpoint");
			return null;
		}
		JSONArray jsonArray = jsonNode.getBody().getArray();
		return jsonArray;
	}
	
}
