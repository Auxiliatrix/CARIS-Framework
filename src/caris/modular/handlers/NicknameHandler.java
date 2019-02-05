package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.UpdateUserReaction;
import caris.modular.reactions.NicknameSetReaction;
import sx.blah.discord.handle.obj.IUser;

public class NicknameHandler extends MessageHandler {

	public NicknameHandler() {
		super("Nickname", false, Access.ADMIN);
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("nickname", "username", "name") && messageEventWrapper.containsAnyWords("lock", "unlock", "set");
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction lockNickname = new MultiReaction(1);
		if( messageEventWrapper.getMessage().getMentions().size() > 0 ) {
			if( messageEventWrapper.containsAnyWords("unlock") ) {
				for( IUser user : messageEventWrapper.getMessage().getMentions() ) {
					lockNickname.add(new UpdateUserReaction(messageEventWrapper.getGuild(), user, "nickname-override", null, true));
				}
				lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), "Nickname" + (messageEventWrapper.getMessage().getMentions().size() > 1 ? "s " : " ") + "unlocked successfully!"));
			} else if( messageEventWrapper.containsAnyWords("lock") ) {
				if( messageEventWrapper.quotedTokens.size() > 0 ) {
					for( IUser user : messageEventWrapper.getMessage().getMentions() ) {
						lockNickname.add(new NicknameSetReaction(messageEventWrapper.getGuild(), user, messageEventWrapper.quotedTokens.get(0)));
						lockNickname.add(new UpdateUserReaction(messageEventWrapper.getGuild(), user, "nickname-override", messageEventWrapper.quotedTokens.get(0), true));
					}
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), "Nickname" + (messageEventWrapper.getMessage().getMentions().size() > 1 ? "s " : " ") + "locked successfully!"));
				} else {
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
				}
			} else if( messageEventWrapper.containsAnyWords("set") ) {
				if( messageEventWrapper.quotedTokens.size() > 0 ) {
					for( IUser user : messageEventWrapper.getMessage().getMentions() ) {
						lockNickname.add(new NicknameSetReaction(messageEventWrapper.getGuild(), user, messageEventWrapper.quotedTokens.get(0)));
					}
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), "Nickname" + (messageEventWrapper.getMessage().getMentions().size() > 1 ? "s " : " ") + "set successfully!"));
				} else {
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
				}
			}
		} else if( messageEventWrapper.containsAnyWords("my") ) {
			if( messageEventWrapper.containsAnyWords("unlock") ) {
				lockNickname.add(new UpdateUserReaction(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), "nickname-override", null, true));
				lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), "Nickname" + (messageEventWrapper.getMessage().getMentions().size() > 1 ? "s " : " ") + "unlocked successfully!"));
			} else if( messageEventWrapper.containsAnyWords("lock") ) {
				if( messageEventWrapper.quotedTokens.size() > 0 ) {
					lockNickname.add(new NicknameSetReaction(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), messageEventWrapper.quotedTokens.get(0)));
					lockNickname.add(new UpdateUserReaction(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), "nickname-override", messageEventWrapper.quotedTokens.get(0), true));
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), "Nickname" + (messageEventWrapper.getMessage().getMentions().size() > 1 ? "s " : " ") + "locked successfully!"));
				} else {
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
				}
			} else if( messageEventWrapper.containsAnyWords("set") ) {
				if( messageEventWrapper.quotedTokens.size() > 0 ) {
					lockNickname.add(new NicknameSetReaction(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), messageEventWrapper.quotedTokens.get(0)));
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), "Nickname set successfully!"));
				} else {
					lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
				}
			}
		} else {
			lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify someone by mentioning them!")));
		}
		return lockNickname;
	}

	@Override
	public String getDescription() {
		return "Locks a user's nickname so that not even administrators can change it.";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add(Constants.NAME + ", set the nicknames of @person and @otherperson to \"nickname\"");
		usage.add(Constants.NAME + ", unlock @person's username");
		return usage;
	}
}
