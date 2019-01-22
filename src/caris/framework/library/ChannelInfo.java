package caris.framework.library;

import java.util.HashMap;

import sx.blah.discord.handle.obj.IChannel;

public class ChannelInfo {
	
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
	
}
