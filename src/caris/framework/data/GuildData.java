package caris.framework.data;

import org.json.JSONObject;

import sx.blah.discord.handle.obj.IGuild;

public class GuildData implements JSONable {

	private long guildID;
	private JSONObject data;
	
	public GuildData(long id) {
		guildID = id;
		data = new JSONObject();
		data.put("guildID", guildID);
	}
	
	public GuildData(IGuild guild) {
		this(guild.getLongID());
	}
	
	public GuildData(JSONObject json) throws JSONReloadException {
		if( !json.has("guildID") || !(json.get("guildID") instanceof Long) ) {
			throw new JSONReloadException();
		}
		data = json;
		guildID = json.getLong("userID");
	}
	
	public Long getGuildID() {
		return guildID;
	}
	
	@Override
	public JSONObject getJSONData() {
		return data;
	}

}
