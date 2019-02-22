package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.events.MessageEventWrapper;
import caris.modular.reactions.MessagePurgeReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class PruneHandler extends MessageHandler {

	public PruneHandler() {
		super("Prune", false, Access.DEFAULT, Permissions.MANAGE_MESSAGES);
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("prune") && messageEventWrapper.containsAnyWords("message", "messages");
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction purgeMessages = new MultiReaction();
		if( messageEventWrapper.integerTokens.size() > 0 ) {
			if( messageEventWrapper.getAllMentionedUsers().size() > 0 ) {
				for( IUser mention : messageEventWrapper.getAllMentionedUsers() ) {
					purgeMessages.add(new MessagePurgeReaction(messageEventWrapper.getChannel(), mention, messageEventWrapper.integerTokens.get(0)));
				}
			} else {
				purgeMessages.add(new MessagePurgeReaction(messageEventWrapper.getChannel(), messageEventWrapper.integerTokens.get(0)));
			}
		} else if( messageEventWrapper.getAllMentionedUsers().size() > 0 ) {
			for( IUser mention : messageEventWrapper.getAllMentionedUsers() ) {
				purgeMessages.add(new MessagePurgeReaction(messageEventWrapper.getChannel(), mention));
			}
		} else {
			purgeMessages.add(new MessagePurgeReaction(messageEventWrapper.getChannel()));
		}
		return purgeMessages;
	}

	@Override
	public String getDescription() {
		return "Prunes messages from the channel.";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add( Constants.NAME + ", prune 15 messages");
		usage.add( Constants.NAME + ", prune @person and @otherperson's messages");
		usage.add( Constants.NAME + ", prune the last 15 messages from @person and @otherperson");
		usage.add( Constants.NAME + ", prune messages. Just like, prune them. As much as you can.");
		return usage;
	}
	
}
