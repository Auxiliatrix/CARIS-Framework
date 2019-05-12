package caris.modular.handlers;

import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageDeleteReaction;
import caris.framework.reactions.UpdateUserReaction;

@Module(name = "ChatMuteEnforce", allowBots = true)
public class ChatMuteEnforceHandler extends MessageHandler {

	public ChatMuteEnforceHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return true;
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		if( Brain.variables.getUserInfo(mew.getGuild(), mew.getAuthor()).userData.has("chat-mute") ) {
			if( (boolean) Brain.variables.getUserInfo(mew.getGuild(), mew.getAuthor()).userData.get("chat-mute") ) {
				return new MessageDeleteReaction(mew.getChannel(), mew.getMessage());
			} else {
				return null;
			}
		} else {
			return new UpdateUserReaction(mew.getGuild(), mew.getAuthor(), "chat-mute", false, true);
		}
	}
	
}
