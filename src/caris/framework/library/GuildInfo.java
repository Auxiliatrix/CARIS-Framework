package caris.framework.library;

import java.util.HashMap;

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
	public String name;
	public IGuild guild;
	
	/* Indices */
	public HashMap<Long, UserInfo> userIndex;
	public HashMap<Long, ChannelInfo> channelIndex;
	public HashMap<SpecialChannel, Long> specialChannels;
	
	/* Modular Info */
	public JSONObject guildData;
	
	public GuildInfo(String name, IGuild guild) {	
		this.name = name;
		this.guild = guild;
				
		userIndex = new HashMap<Long, UserInfo>();
		channelIndex = new HashMap<Long, ChannelInfo>();
		specialChannels = new HashMap<SpecialChannel, Long>();
				
		this.guildData = new JSONObject();
		init();
	}
	
	private void init() {
		for( IUser u : guild.getUsers() ) {
			addUser(u);
		}
		for( IChannel c : guild.getChannels() ) {
			addChannel(c);
		}
	}
	
	public void addUser( IUser u ) {
		userIndex.put( u.getLongID(), new UserInfo(u) );
		if( !Brain.variables.globalUserIndex.containsKey(u.getLongID()) ) {
			Brain.variables.globalUserIndex.put(u.getLongID(), new GlobalUserInfo(u));
		}
	}
	
	public void addChannel( IChannel c ) {
		channelIndex.put( c.getLongID(), new ChannelInfo(c));
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
			return guild.getChannelByID(specialChannels.get(SpecialChannel.DEFAULT));
		} else {
			return guild.getDefaultChannel();
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
		return JSONData;
	}
}
