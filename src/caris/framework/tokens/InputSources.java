package caris.framework.tokens;

import java.util.ArrayList;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class InputSources {

	public ArrayList<IGuild> guilds;
	public ArrayList<IChannel> channels;

	public InputSources() {
		this.guilds = new ArrayList<IGuild>();
		this.channels = new ArrayList<IChannel>();
	}
	
	public InputSources(IGuild...guilds) {
		this();
		add(guilds);
	}
	
	public InputSources(IChannel...channels) {
		this();
		add(channels);
	}
	
	public void add(InputSources inputSources) {
		channels.addAll(inputSources.channels);
		guilds.addAll(inputSources.guilds);
	}
	
	public void add(IGuild...guilds) {
		for( IGuild guild : guilds ) {
			this.guilds.add(guild);
		}
	}
	
	public void add(IChannel...channels) {
		for( IChannel channel : channels ) {
			this.channels.add(channel);
		}
	}
	
	public void remove(InputSources inputSources) {
		channels.removeAll(inputSources.channels);
		guilds.removeAll(inputSources.guilds);
	}
	
	public void remove(IGuild...guilds) {
		for( IGuild guild : guilds ) {
			this.guilds.remove(guild);
		}
	}
	
	public void remove(IChannel...channels) {
		for( IChannel channel : channels ) {
			this.channels.remove(channel);
		}
	}
	
}
