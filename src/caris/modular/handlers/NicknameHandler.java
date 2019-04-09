package caris.modular.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.UpdateUserReaction;
import caris.modular.reactions.NicknameSetReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "Nickname")
@Help(
		category = "Default",
		description = "Sets, locks, and unlocks people's nicknames. Locking a nickname makes it so that not even admins can change it.",
		usage = {
				Constants.NAME + ", set @person nickname to \"nickname\"",
				Constants.NAME + ", lock @person's username to \"nickname\"",
				Constants.NAME + ", unlock the username of @person"
		}
	)
public class NicknameHandler extends MessageHandler {

	public NicknameHandler() {
		super();
	}

	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return (invoked(mew) || mentioned(mew)) && mew.containsAnyWords("nickname", "username", "name") && mew.containsAnyWords("lock", "unlock", "set", "change");
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		MultiReaction lockNickname = new MultiReaction(1);
		if( mew.getMessage().getMentions().size() > 0 ) {
			if( getPosition(mew, mew.getAuthor()) <= getPosition(mew, mew.getMessage().getMentions().get(0)) && !mew.developerAuthor) {
				lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.ACCESS, "You don't have permission to modify this user's nickname!")));
			} else if( !Brain.cli.getOurUser().getPermissionsForGuild(mew.getGuild()).contains(Permissions.MANAGE_NICKNAMES) || getBotPosition(mew) <= getPosition(mew, mew.getMessage().getMentions().get(0)) ) {
				lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.PERMISSION, "I don't have permission to modify this user's nickname!")));
			} else {
				if( mew.containsAnyWords("unlock") ) {
					for( IUser user : mew.getMessage().getMentions() ) {
						lockNickname.add(new UpdateUserReaction(mew.getGuild(), user, "nickname-override", null, true));
					}
					lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname" + (mew.getMessage().getMentions().size() > 1 ? "s " : " ") + "unlocked successfully!"));
				} else if( mew.containsAnyWords("lock") ) {
					if( mew.quotedTokens.size() > 0 ) {
						for( IUser user : mew.getMessage().getMentions() ) {
							lockNickname.add(new NicknameSetReaction(mew.getGuild(), user, mew.quotedTokens.get(0)));
							lockNickname.add(new UpdateUserReaction(mew.getGuild(), user, "nickname-override", mew.quotedTokens.get(0), true));
						}
						lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname" + (mew.getMessage().getMentions().size() > 1 ? "s " : " ") + "locked successfully!"));
					} else {
						lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
					}
				} else if( mew.containsAnyWords("set") ) {
					if( mew.quotedTokens.size() > 0 ) {
						for( IUser user : mew.getMessage().getMentions() ) {
							lockNickname.add(new NicknameSetReaction(mew.getGuild(), user, mew.quotedTokens.get(0)));
						}
						lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname" + (mew.getMessage().getMentions().size() > 1 ? "s " : " ") + "set successfully!"));
					} else {
						lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
					}
				}
			}
		} else if( mew.containsAnyWords("my") ) {
			if( !mew.getAuthor().getPermissionsForGuild(mew.getGuild()).contains(Permissions.CHANGE_NICKNAME) && !mew.developerAuthor ) {
				lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.ACCESS, "You don't have permission to modify your nickname!")));
			}
			if( !Brain.cli.getOurUser().getPermissionsForGuild(mew.getGuild()).contains(Permissions.CHANGE_NICKNAME) || getBotPosition(mew) <= getPosition(mew, mew.getAuthor()) ) {
				lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.PERMISSION, "I don't have permission to modify your nickname!")));
			} else {
				if( mew.containsAnyWords("unlock") ) {
					lockNickname.add(new UpdateUserReaction(mew.getGuild(), mew.getAuthor(), "nickname-override", null, true));
					lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname" + (mew.getMessage().getMentions().size() > 1 ? "s " : " ") + "unlocked successfully!"));
				} else if( mew.containsAnyWords("lock") ) {
					if( mew.quotedTokens.size() > 0 ) {
						lockNickname.add(new NicknameSetReaction(mew.getGuild(), mew.getAuthor(), mew.quotedTokens.get(0)));
						lockNickname.add(new UpdateUserReaction(mew.getGuild(), mew.getAuthor(), "nickname-override", mew.quotedTokens.get(0), true));
						lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname" + (mew.getMessage().getMentions().size() > 1 ? "s " : " ") + "locked successfully!"));
					} else {
						lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
					}
				} else if( mew.containsAnyWords("set", "change") ) {
					if( mew.quotedTokens.size() > 0 ) {
						lockNickname.add(new NicknameSetReaction(mew.getGuild(), mew.getAuthor(), mew.quotedTokens.get(0)));
						lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname set successfully!"));
					} else {
						lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
					}
				}
			}
		} else if( mew.containsAnyWords("your") ) {
			if( getPosition(mew, mew.getAuthor()) <= getPosition(mew, Brain.cli.getOurUser()) && !mew.developerAuthor) {
				lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.ACCESS, "You don't have permission to modify this user's nickname!")));
			} else if( !Brain.cli.getOurUser().getPermissionsForGuild(mew.getGuild()).contains(Permissions.CHANGE_NICKNAME) ) {
				lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.PERMISSION, "I don't have permission to modify this user's nickname!")));
			} else {
				if( mew.containsAnyWords("unlock") ) {
					lockNickname.add(new UpdateUserReaction(mew.getGuild(), Brain.cli.getOurUser(), "nickname-override", null, true));
					lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname" + "unlocked successfully!"));
				} else if( mew.containsAnyWords("lock") ) {
					if( mew.quotedTokens.size() > 0 ) {
						lockNickname.add(new NicknameSetReaction(mew.getGuild(), Brain.cli.getOurUser(), mew.quotedTokens.get(0)));
						lockNickname.add(new UpdateUserReaction(mew.getGuild(), Brain.cli.getOurUser(), "nickname-override", mew.quotedTokens.get(0), true));
						lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname" + (mew.getMessage().getMentions().size() > 1 ? "s " : " ") + "locked successfully!"));
					} else {
						lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
					}
				} else if( mew.containsAnyWords("set") ) {
					if( mew.quotedTokens.size() > 0 ) {
						lockNickname.add(new NicknameSetReaction(mew.getGuild(), Brain.cli.getOurUser(), mew.quotedTokens.get(0)));
						lockNickname.add(new MessageReaction(mew.getChannel(), "Nickname set successfully!"));
					} else {
						lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify a nickname in quotes!")));
					}
				}
			}
		} else {
			lockNickname.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorBuilder.ErrorType.SYNTAX, "You must specify someone by mentioning them!")));
		}
		return lockNickname;
	}
	
}
