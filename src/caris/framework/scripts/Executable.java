package caris.framework.scripts;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public abstract class Executable {
		
	public abstract Reaction execute(MessageEventWrapper mew, Context context) throws ScriptExecutionException;
	
	protected void breakIfIllegal(IGuild guild, IUser invoker, IUser target, boolean override, Permissions permission, String action) throws ScriptExecutionException {
		if( !Brain.cli.getOurUser().getPermissionsForGuild(guild).contains(permission) ) {
			throw new ScriptExecutionException("I don't have permission to " + action + " others!");
		} if( MessageHandler.getBotPosition(guild) <= MessageHandler.getPosition(guild, target) && Brain.cli.getOurUser() != target) {
			throw new ScriptExecutionException("I don't have permission to " + action + " " + target.getName() + "!");
		} else if( !target.getPermissionsForGuild(guild).contains(permission) && !override && !MessageHandler.developerAuthor(invoker) ) {
			throw new ScriptExecutionException("You don't have permission to " + action + " others!");
		} else if( MessageHandler.getPosition(guild, invoker) <= MessageHandler.getPosition(guild, target) && !override && invoker != target && !MessageHandler.developerAuthor(invoker) ) {
			throw new ScriptExecutionException("You don't have permission to " + action + " " + target.getName() + "!");
		}
	}
	
	protected void breakIfIllegal(IGuild guild, IUser invoker, IUser target, boolean override, IRole role, String action) throws ScriptExecutionException {
		breakIfIllegal(guild, invoker, target, override, Permissions.MANAGE_ROLES, action);
		if( MessageHandler.getBotPosition(guild) <= role.getPosition() ) {
			throw new ScriptExecutionException("I don't have permission to manage that role!");
		} else if( MessageHandler.getPosition(guild, invoker) <= role.getPosition() && !override && !MessageHandler.developerAuthor(invoker) ) {
			throw new ScriptExecutionException("You don't have permission to manage that role!");
		}
	}
	
	@SuppressWarnings("serial")
	public static class ScriptExecutionException extends Exception {
		
		private EmbedObject errorEmbed;
		
		public ScriptExecutionException() {
			this(ErrorType.EXECUTION, "Script Execution Error!");
		}
		
		public ScriptExecutionException(ErrorType errorType) {
			this(errorType, "Script Execution Error!");
		}
		
		public ScriptExecutionException(String errorMessage) {
			this(ErrorType.EXECUTION, errorMessage);
		}
		
		public ScriptExecutionException( ErrorType errorType, String errorMessage ) {
			super(errorMessage);
			errorEmbed = ErrorBuilder.getErrorEmbed(errorType, errorMessage);
		}
		
		public EmbedObject getErrorEmbed() {
			return errorEmbed;
		}
		
	}
	
}
