package caris.framework.scripts;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.ScriptModule;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.controls.Executable_MULTI;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class ScriptCompiler {

	public static final ScriptModule compileScript(String script, IGuild guild) throws ScriptCompilationException {
		// TODO: actual compilation
		throw new ScriptCompilationException();
		//return null;
	}
	
	public static final Executable compileCode(String string) {
		List<Executable> compiledCode = new ArrayList<Executable>();
		// TODO: actual compilation
		return new Executable_MULTI(compiledCode.toArray(new Executable[compiledCode.size()]));
	}
	
	public static final String compileFormattedString(MessageEventWrapper mew, Context context, String string) {
		// TODO: actual compilation
		return "";
	}
	
	public static final String compileStringVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual compilation
		return "";
	}
	
	public static final int compileIntVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual compilation
		return 0;
	}
	
	public static final IUser compileUserVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual compilation
		return null;
	}
	
	public static final IRole compileRoleVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual compilation
		return null;
	}
	
	public static final boolean compileBooleanVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual compilation
		return false;
	}
	
	public static final List<String> compileStringIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<String> stringIterable = new ArrayList<String>();
		// TODO: actual compilation
		return stringIterable;
	}
	
	public static final List<Integer> compileIntIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<Integer> intIterable = new ArrayList<Integer>();
		// TODO: actual compilation
		return intIterable;
	}
	
	public static final List<IUser> compileUserIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<IUser> userIterable = new ArrayList<IUser>();
		// TODO: actual compilation
		return userIterable;
	}
	
	public static final List<IRole> compileRoleIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<IRole> roleIterable = new ArrayList<IRole>();
		// TODO: actual compilation
		return roleIterable;
	}
	
	@SuppressWarnings("serial")
	public static class ScriptCompilationException extends Exception {
		
		private EmbedObject errorEmbed;
		
		public ScriptCompilationException() {
			this(ErrorType.DEFAULT, "Script Compilation Error!");
		}
		
		public ScriptCompilationException(ErrorType errorType, String errorMessage) {
			super(errorMessage);
			this.errorEmbed = ErrorBuilder.getErrorEmbed(errorType, errorMessage);
		}
		
		public EmbedObject getErrorEmbed() {
			return errorEmbed;
		}
		
	}
	
}
