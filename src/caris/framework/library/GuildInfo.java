package caris.framework.library;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import caris.framework.calibration.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;


public class GuildInfo implements JSONable {
	
	public enum SpecialChannel {
		DEFAULT {
			@Override
			public String toString() {
				return "DEFAULT";
			}
		},
		LOG {
			@Override
			public String toString() {
				return "LOG";
			}
		},
	}
	
	/* Basic Information */
	public Long guildID;
	
	/* Indices */
	public HashMap<Long, UserInfo> userIndex;
	public HashMap<Long, ChannelInfo> channelIndex;
	public HashMap<SpecialChannel, Long> specialChannels;
	
	/* Modular Info */
	public JSONObject guildData;
	
	public GuildInfo(JSONObject json) throws JSONReloadException {
		this();
		if( json != null ) {
			try {
				guildID = json.getLong("guildID");
			} catch (JSONException e){
				e.printStackTrace();
				throw new JSONReloadException();
			}
			try {
				guildData = json.getJSONObject("guildData");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				JSONObject JSONuserIndex = json.getJSONObject("userIndex");
				for( Object key : JSONuserIndex.keySet() ) {
					try {
						userIndex.put(Long.parseLong(key.toString()), new UserInfo(JSONuserIndex.getJSONObject(key.toString())));
					} catch (JSONReloadException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
			try {
				JSONObject JSONchannelIndex = json.getJSONObject("channelIndex");
				for( Object key : JSONchannelIndex.keySet() ) {
					try {
						channelIndex.put(Long.parseLong(key.toString()), new ChannelInfo(JSONchannelIndex.getJSONObject(key.toString())));
					} catch (JSONReloadException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
			try {
				JSONObject JSONspecialChannels = json.getJSONObject("specialChannels");
				for( Object key : specialChannels.keySet() ) {
					for( SpecialChannel sc : SpecialChannel.values() ) {
						if( key.toString().equals(sc.toString()) ) {
							specialChannels.put(sc, JSONspecialChannels.getLong(key.toString()));
						}
					}
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
		} else {
			throw new JSONReloadException();
		}
	}
	
	public GuildInfo(IGuild guild) {	
		this();
		this.guildID = guild.getLongID();
		loadUsers();
		loadChannels();
	}
	
	private GuildInfo() {
		userIndex = new HashMap<Long, UserInfo>();
		channelIndex = new HashMap<Long, ChannelInfo>();
		specialChannels = new HashMap<SpecialChannel, Long>();
		
		this.guildData = new JSONObject();
	}
	
	public void reload() {
		loadUsers();
		loadChannels();
	}
	
	private void loadUsers() {
		for( IUser u : Brain.cli.getGuildByID(guildID).getUsers() ) {
			addUser(u);
		}
	}
	
	private void loadChannels() {
		for( IChannel c : Brain.cli.getGuildByID(guildID).getChannels() ) {
			addChannel(c);
		}
	}
	
	public void addUser( IUser u ) {
		if( !userIndex.containsKey(u.getLongID()) ) {
			userIndex.put( u.getLongID(), new UserInfo(u) );
		}
		if( !Brain.variables.globalUserIndex.containsKey(u.getLongID()) ) {
			Brain.variables.globalUserIndex.put(u.getLongID(), new GlobalUserInfo(u));
		}
	}
	
	public void addChannel( IChannel c ) {
		if( !channelIndex.containsKey(c.getLongID()) ) {
			channelIndex.put( c.getLongID(), new ChannelInfo(c));
		}
	}
	
	public boolean checkDisabled(String module) {
		for( String s : Constants.DEFAULT_DISBABLED ) {
			if( s.equals(module) ) {
				return false;
			}
		}
		return true;
	}
	
	public IChannel getDefaultChannel() {
		if( specialChannels.containsKey(SpecialChannel.DEFAULT) ) {
			return Brain.cli.getChannelByID(specialChannels.get(SpecialChannel.DEFAULT));
		} else {
			return Brain.cli.getGuildByID(guildID).getDefaultChannel();
		}
	}

	@Override
	public JSONObject getJSONData() {
		JSONObject JSONData = new JSONObject();
		JSONObject JSONuserIndex = new JSONObject();
		JSONObject JSONchannelIndex = new JSONObject();
		JSONObject JSONspecialChannels = new JSONObject();
		for( Long key : userIndex.keySet() ) {
			JSONuserIndex.put(key.toString(), userIndex.get(key).getJSONData());
		}
		for( Long key : channelIndex.keySet() ) {
			JSONchannelIndex.put(key.toString(), channelIndex.get(key).getJSONData());
		}
		for( SpecialChannel key : specialChannels.keySet() ) {
			JSONspecialChannels.put(key.toString(), specialChannels.get(key));
		}
		JSONData.put("userIndex", JSONuserIndex);
		JSONData.put("channelIndex", JSONchannelIndex);
		JSONData.put("specialChannels", JSONspecialChannels);
		JSONData.put("guildData", guildData);
		JSONData.put("guildID", guildID);
		return JSONData;
	}
}
