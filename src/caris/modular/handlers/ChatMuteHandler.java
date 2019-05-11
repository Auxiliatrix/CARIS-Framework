package caris.modular.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.UpdateUserReaction;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

@Module(name = "ChatMute")
@Help(
		category = "Admin",
		description = "Mutes people's messages, making it so that their messages are deleted immediately after they are sent.",
		usage = {
				Constants.NAME + ", mute @person in chat, please",
				Constants.NAME + ", just chat mute anyone with the @annoying role, thanks.",
				Constants.NAME + ", chat unmute @person now",
				Constants.NAME + ", can you unmute @person1 and @person2 for good behavior?"
		}
	)
public class ChatMuteHandler extends MessageHandler {

	public ChatMuteHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return mentioned(mew) && mew.containsAnyWords("mute", "unmute") && mew.containsWord("chat");
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		MultiReaction chatMute = new MultiReaction();
		if( mew.getMessage().getMentions().size() > 0  ) {
			boolean muteState = !mew.containsWord("unmute");
			for( IUser user : mew.getMessage().getMentions() ) {
				if( getPosition(mew.getGuild(), mew.getAuthor()) <= getPosition(mew.getGuild(), user) ) {
					chatMute.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.PERMISSION, "You don't have permission to chatmute the user\"" + user.getName() + "\"!")));
					continue;
				}
				chatMute.add(new UpdateUserReaction(mew.getGuild(), user, "chat-mute", muteState, true));
			}
		} else if( mew.getMessage().getRoleMentions().size() > 0 ) {
			boolean muteState = !mew.containsWord("unmute");
			for( IRole role : mew.getMessage().getRoleMentions() ) {
				if( getPosition(mew.getGuild(), mew.getAuthor()) <= role.getPosition() ) {
					chatMute.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.PERMISSION, "You don't have permission to chatmute the role\"" + role.getName() + "\"!")));
					continue;
				}
				for( IUser user : mew.getGuild().getUsersByRole(role) ) {
					chatMute.add(new UpdateUserReaction(mew.getGuild(), user, "chat-mute", muteState, true));
				}
			}
		} else {
			chatMute.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify someone by mentioning them!")));
		}
		return chatMute;
	}
	
	

}
