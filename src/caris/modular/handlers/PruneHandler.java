package caris.modular.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.modular.reactions.MessagePurgeReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "Prune")
@Help(
		category = "Admin",
		description = "Prunes messages from the channel.",
		usage = {
					Constants.NAME + ", prune 15 messages",
					Constants.NAME + ", prune @person and @otherperson's messages",
					Constants.NAME + ", prune the last 15 messages from @person and @otherperson",
					Constants.NAME + ", prune messages. Just like, prune them. As much as you can."
		}
	)
public class PruneHandler extends MessageHandler {

	public PruneHandler() {
		super(Permissions.MANAGE_MESSAGES);
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
	
}
