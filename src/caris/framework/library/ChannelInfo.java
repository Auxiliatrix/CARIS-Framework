package caris.framework.library;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IChannel;

public class ChannelInfo implements JSONable {
	
	/* Basic Info */
	public String name;
	public IChannel channel;
	
	/* Modular Info */
	public JSONObject channelData;
	
	public ChannelInfo( IChannel channel ) {
		this.name = channel.getName();
		this.channel = channel;
		
		this.channelData = new JSONObject();
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONData.put("channelData", channelData);
		return JSONData;
	}
	
}
