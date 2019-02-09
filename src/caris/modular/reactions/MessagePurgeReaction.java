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
	
	public MessagePurgeReaction(IChannel channel, IUser user) {
		this(channel, user, -1, 1);
	}
	
	public MessagePurgeReaction(IChannel channel, int number) {
		this(channel, null, number, 1);
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
		if( user == null ) {
			if( number > 0 ) {
				purgeList.addAll(channel.getMessageHistory(number));
			} else {
				purgeList.addAll(channel.getMessageHistory());
			}
		} else {
			for( IMessage message : channel.getMessageHistory() ) {
				if( message.getAuthor() == user ) {
					purgeList.add(message);
				}
			}
		}
		channel.bulkDelete(purgeList);
	}
	
}
