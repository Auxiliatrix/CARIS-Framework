package caris.framework.library;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.obj.IChannel;

public class ChannelInfo implements JSONable {
	
	/* Basic Info */
	public Long channelID;
	
	/* Modular Info */
	public JSONObject channelData;
	
	public ChannelInfo( JSONObject json ) throws JSONReloadException {
		this();
		if( json != null ) {
			try {
				channelID = json.getLong("channelID");
			} catch (JSONException e) {
				e.printStackTrace();
				throw new JSONReloadException();
			}
			try {
				channelData = json.getJSONObject("channelData");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			throw new JSONReloadException();
		}
	}
	
	public ChannelInfo( IChannel channel ) {
		this();
		this.channelID = channel.getLongID();
	}
	
	private ChannelInfo() {
		this.channelData = new JSONObject();
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONData.put("channelData", channelData);
		JSONData.put("channelID", channelID);
		return JSONData;
	}
	
}
