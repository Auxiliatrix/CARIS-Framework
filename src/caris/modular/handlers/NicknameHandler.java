package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.calibration.Constants;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.UpdateUserReaction;
import caris.modular.reactions.NicknameSetReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class NicknameHandler extends MessageHandler {

	public NicknameHandler() {
		super("Nickname", false, Access.DEFAULT, Permissions.MANAGE_NICKNAMES);
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return (invoked(messageEventWrapper) || mentioned(messageEventWrapper)) && messageEventWrapper.containsAnyWords("nickname", "username", "name") && messageEventWrapper.containsAnyWords("lock", "unlock", "set", "change");
	}

	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction lockNickname = new MultiReaction(1);
		if( messageEventWrapper.getMessage().getMentions().size() > 0 ) {
			if( getPosition(messageEventWrapper, messageEventWrapper.getAuthor()) <= getPosition(messageEventWrapper, messageEventWrapper.getMessage().getMentions().get(0)) && !messageEventWrapper.developerAuthor) {
				lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.ACCESS, "You don't have permission to modify this user's nickname!")));
			} else if( !Brain.cli.getOurUser().getPermissionsForGuild(messageEventWrapper.getGuild()).contains(Permissions.MANAGE_NICKNAMES) || getBotPosition(messageEventWrapper) <= getPosition(messageEventWrapper, messageEventWrapper.getMessage().getMentions().get(0)) ) {
				lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.PERMISSION, "I don't have permission to modify this user's nickname!")));
			} else {
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
			}
		} else if( messageEventWrapper.containsAnyWords("my") ) {
			if( !messageEventWrapper.getAuthor().getPermissionsForGuild(messageEventWrapper.getGuild()).contains(Permissions.CHANGE_NICKNAME) && !messageEventWrapper.developerAuthor ) {
				lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.ACCESS, "You don't have permission to modify your nickname!")));
			}
			if( !Brain.cli.getOurUser().getPermissionsForGuild(messageEventWrapper.getGuild()).contains(Permissions.CHANGE_NICKNAME) || getBotPosition(messageEventWrapper) <= getPosition(messageEventWrapper, messageEventWrapper.getAuthor()) ) {
				lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.PERMISSION, "I don't have permission to modify your nickname!")));
			} else {
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
				} else if( messageEventWrapper.containsAnyWords("set", "change") ) {
					if( messageEventWrapper.quotedTokens.size() > 0 ) {
						lockNickname.add(new NicknameSetReaction(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), messageEventWrapper.quotedTokens.get(0)));
						lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), "Nickname set successfully!"));
					} else {
						lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
					}
				}
			}
		} else {
			lockNickname.add(new MessageReaction(messageEventWrapper.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify someone by mentioning them!")));
		}
		return lockNickname;
	}

	@Override
	public String getDescription() {
		return "Sets, locks, and unlocks people's nicknames. Locking a nickname makes it so that not even admins can change it.";
	}
	
	@Override
	public List<String> getUsage() {
		List<String> usage = new ArrayList<String>();
		usage.add(Constants.NAME + ", set @person nickname to \"nickname\"");
		usage.add(Constants.NAME + ", lock @person's username to \"nickname\"");
		usage.add(Constants.NAME + ", unlock the username of @person");
		return usage;
	}
}
