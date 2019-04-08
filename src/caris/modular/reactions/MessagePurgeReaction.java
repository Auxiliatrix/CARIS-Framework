package caris.modular.reactions;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class MessagePurgeReaction extends Reaction {

	public IChannel channel;
	public IUser user;
	public int number;
	
	public MessagePurgeReaction(IChannel channel) {
		this(channel, null, -1, 1);
	}
	
	public MessagePurgeReaction(IChannel channel, int number) {
		this(channel, null, number, 1);
	}
	
	public MessagePurgeReaction(IChannel channel, IUser user) {
		this(channel, user, -1, 1);
	}
	
	public MessagePurgeReaction(IChannel channel, IUser user, int number) {
		this(channel, user, number, 1);
	}
	
	public MessagePurgeReaction(IChannel channel, IUser user, int number, int priority) {
		super(priority);
		this.channel = channel;
		this.user = user;
		this.number = number;
	}

	@Override
	public void process() {
		List<IMessage> purgeList = new ArrayList<IMessage>();
		int purgeCount = 0;
		for( IMessage message : new ArrayList<IMessage>(channel.getMessageHistory()) ) {
			if( user == null || message.getAuthor() == user) {
				purgeList.add(message);
				purgeCount++;
			}
			if( purgeCount >= number ) {
				break;
			}
		}
		if( purgeList.size() > 0 ) {
			channel.bulkDelete(purgeList);
		}
	}
	
}
