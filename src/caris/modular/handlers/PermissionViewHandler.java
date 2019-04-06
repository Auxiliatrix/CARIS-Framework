package caris.modular.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;
import caris.modular.embedbuilders.PermissionsBuilder;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

@Module(name = "PermissionViewHandler")
@Help(
		category = "Default",
		description = "Views a user or role's permissions",
		usage = {
				Constants.NAME + ", what permissions does <@person | \"username\"> have?",
				Constants.NAME + ", what permissions does <@role | \"rolename\"> have?"
		}
	)
public class PermissionViewHandler extends MessageHandler {

	public PermissionViewHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return mentioned(mew) && mew.containsAnyWords("perms", "permissions", "power");
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		if( mew.getMessage().getRoleMentions().size() != 0 ) {
			return new MessageReaction(mew.getChannel(), PermissionsBuilder.getPermissionsEmbed(mew.getMessage().getRoleMentions().get(0)));
		} else if( mew.getMessage().getMentions().size() != 0 ) {
			return new MessageReaction(mew.getChannel(), PermissionsBuilder.getPermissionsEmbed(mew.getMessage().getMentions().get(0), mew.getGuild()));
		} else if( mew.quotedTokens.size() != 0 ) {
			for( String quoted : mew.quotedTokens ) {
				for( IRole role : mew.getGuild().getRoles() ) {
					if( quoted.equalsIgnoreCase(role.getName()) ) {
						return new MessageReaction(mew.getChannel(), PermissionsBuilder.getPermissionsEmbed(role));
					}
				}
				for( IUser user : mew.getGuild().getUsers() ) {
					if( quoted.equalsIgnoreCase(user.getName()) ) {
						return new MessageReaction(mew.getChannel(), PermissionsBuilder.getPermissionsEmbed(user, mew.getGuild()));
					}
				}
			}
			return new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorType.KEYWORD, "Role / User not found!"));
		} else if( mew.containsAnyWords("my", "I") ) {
			return new MessageReaction(mew.getChannel(), PermissionsBuilder.getPermissionsEmbed(mew.getAuthor(), mew.getGuild()));
		} else if( mew.containsAnyWords("your", "you") ) {
			return new MessageReaction(mew.getChannel(), PermissionsBuilder.getPermissionsEmbed(Brain.cli.getOurUser(), mew.getGuild()));
		} else {
			return new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorType.SYNTAX, "You must specify a valid Role / User!"));
		}
	}

}
