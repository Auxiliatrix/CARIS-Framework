package caris.modular.reactions;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class BlackboxPurgeReaction extends Reaction {

	public IChannel channel;
	public List<Long> messages;
	
	public BlackboxPurgeReaction(IChannel channel, List<Long> messages) {
		this.channel = channel;
		this.messages = messages;
	}
	
	@Override
	public void process() {
		List<IMessage> purgeList = new ArrayList<IMessage>();
		for( Long id : messages ) {
			IMessage message = channel.getMessageByID(id);
			if( message != null ) {
				purgeList.add(message);
			}
		}
		channel.bulkDelete(purgeList);
	}

}
