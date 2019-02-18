package caris.framework.library;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Variables implements JSONable {
	

	// Dynamic global variables
	public AtomicReference<JSONObject> atomicVariableData;
	
	/* Gigantic Variable Library */
	private ConcurrentHashMap<Long, GuildInfo> guildIndex;
	
	/* Global UserData */
	private ConcurrentHashMap<Long, GlobalUserInfo> globalUserIndex;
	
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

	public void addGuild(IGuild guild) {
		if( !guildIndex.containsKey(guild.getLongID()) ) {
			guildIndex.put(guild.getLongID(), new GuildInfo(guild));
		} else {
			guildIndex.get(guild.getLongID()).reload();
		}
	}
	
	public void addChannel(IChannel channel) {
		getGuildInfo(channel.getGuild()).addChannel(channel);
	}
	
	public void addUser(IGuild guild, IUser user) {
		getGuildInfo(guild).addUser(user);
		if( !globalUserIndex.containsKey(user.getLongID()) ) {
			globalUserIndex.put(user.getLongID(), new GlobalUserInfo(user));
		}
	}
	
	public GuildInfo getGuildInfo(IGuild guild) {
		return guildIndex.get(guild.getLongID());
	}
	
	public GuildInfo getGuildInfo(IMessage message) {
		return getGuildInfo(message.getGuild());
	}
	
	public ChannelInfo getChannelInfo(IChannel channel) {
		return getGuildInfo(channel.getGuild()).getChannelInfo(channel);
	}
	
	public ChannelInfo getChannelInfo(IMessage message) {
		return getChannelInfo(message.getChannel());
	}
	
	public UserInfo getUserInfo(IGuild guild, IUser user) {
		return getGuildInfo(guild).getUserInfo(user);
	}
	
	public UserInfo getUserInfo(IMessage message) {
		return getUserInfo(message.getGuild(), message.getAuthor());
	}
	
	public GlobalUserInfo getGlobalUserInfo(IUser user) {
		return globalUserIndex.get(user.getLongID());
	}
	
	public GlobalUserInfo getGlobalUserInfo(IMessage message) {
		return globalUserIndex.get(message.getAuthor().getLongID());
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
