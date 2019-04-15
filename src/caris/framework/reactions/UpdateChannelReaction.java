package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IChannel;

public class UpdateChannelReaction extends Reaction {

	public IChannel channel;
	public String key;
	public Object value;
	public boolean override;
	
	public UpdateChannelReaction(IChannel channel, String key, Object value) {
		this(channel, key, value, false, -1);
	}
	
	public UpdateChannelReaction(IChannel channel, String key, Object value, boolean override) {
		this(channel, key, value, override, -1);
	}
	
	public UpdateChannelReaction(IChannel channel, String key, Object value, int priority) {
		this(channel, key, value, false, priority);
	}
	
	public UpdateChannelReaction(IChannel channel, String key, Object value, boolean override, int priority) {
		super(priority);
		this.channel = channel;
		this.key = key;
		this.value = value;
		this.override = override;
	}
	
	@Override
	public void process() {
		if( !Brain.variables.getChannelInfo(channel).channelData.has(key) || override ) {
			if( value == null ) {
				Brain.variables.getChannelInfo(channel).channelData.remove(key);
			} else {
				Brain.variables.getChannelInfo(channel).channelData.put(key, value);
			}
		}
	}

}
