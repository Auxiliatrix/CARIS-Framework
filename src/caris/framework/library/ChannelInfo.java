package caris.framework.library;

import java.util.HashMap;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IChannel;

public class ChannelInfo implements JSONable {
	
	/* Basic Info */
	public String name;
	public IChannel channel;
	
	/* Modular Info */
	public HashMap<String, Object> channelData;
	
	public ChannelInfo( IChannel channel ) {
		this.name = channel.getName();
		this.channel = channel;
		
		this.channelData = new HashMap<String, Object>();
	}

	@Override
	public JSONObject getJSONData() {
		HashMap<String, JSONObject> JSONData = new HashMap<String, JSONObject>();
		JSONData.put("channelData", JSONify(channelData));
		return new JSONObject(JSONData);
	}
	
}
