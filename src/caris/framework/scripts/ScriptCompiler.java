package caris.framework.scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import caris.framework.basehandlers.ScriptModule;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.commands.Executable_BAN;
import caris.framework.scripts.commands.Executable_DEAFEN;
import caris.framework.scripts.commands.Executable_DEMOTE;
import caris.framework.scripts.commands.Executable_KICK;
import caris.framework.scripts.commands.Executable_MUTE;
import caris.framework.scripts.commands.Executable_NICK;
import caris.framework.scripts.commands.Executable_PROMOTE;
import caris.framework.scripts.commands.Executable_SAY;
import caris.framework.scripts.commands.Executable_STOP;
import caris.framework.scripts.commands.Executable_UNDEAFEN;
import caris.framework.scripts.commands.Executable_UNMUTE;
import caris.framework.scripts.commands.Executable_WAIT;
import caris.framework.scripts.controls.Executable_EACH;
import caris.framework.scripts.controls.Executable_FOR;
import caris.framework.scripts.controls.Executable_IF;
import caris.framework.scripts.controls.Executable_MULTI;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class ScriptCompiler {
	
	public static final ScriptModule compileScript(String script, IGuild guild) throws ScriptCompilationException {
		String[] lines = script.split("\n");
		if( lines.length < 2 ) {
			throw new ScriptCompilationException("Script must have a header and at least one line of code!");
		}
		String header = lines[0];
		String[] code = Arrays.copyOfRange(lines, 1, lines.length);
		if( header.charAt(0) != header.charAt(header.length()-1) ) {
			throw new ScriptCompilationException("Script is missing a valid header!");
		}
		Executable compiledCode = compileCode(code);
		switch (header.charAt(0)) {
			case '%':
				return new ScriptModule(header.substring(1, header.length()-1), false, null, compiledCode, script);
			case '=':
				return new ScriptModule(header.substring(1, header.length()-1), true, null, compiledCode, script);
			case '+':
				return new ScriptModule(header.substring(1, header.length()-1), false, guild, compiledCode, script);
			case '-':
				return new ScriptModule(header.substring(1, header.length()-1), true, guild, compiledCode, script);
			default:
				throw new ScriptCompilationException("Script is missing a valid header!");
		}
	}
	
	public static final Executable compileCode(String[] code) throws ScriptCompilationException {
		List<Executable> compiledCode = new ArrayList<Executable>();
		for( int f=0; f<code.length; f++ ) {
			String line = code[f];
			if( line.startsWith("|") ) {
				continue;
			}
			boolean override = line.startsWith("~") && line.length() > 1;
			if( override ) {
				line = line.substring(1);
			}
			if( line.length() == 0 ) {
				continue;
			}
			String[] tokens = line.split(" ");
			if( tokens[0].equals("Stop") ) {
				compiledCode.add(new Executable_STOP());
			} else if( tokens[0].equals("If") || tokens[0].equals("For") || tokens[0].equals("Each") ) {
				int end = -1;
				for( int g=f+1; g<code.length; g++ ) {
					if( code[g].equals("End") ) {
						end = g;
						break;
					}
				}
				if( end != -1 ) {
					String[] bodyCode = Arrays.copyOfRange(code, f+1, end);
					Executable body = compileCode(bodyCode);
					switch (tokens[0]) {
						case "If":
							if( tokens.length < 2 ) {
								throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": If structure must specify a Condition!");
							}
							compiledCode.add(new Executable_IF(line.substring(3), body));
							break;
						case "For":
							if( tokens.length < 3 ) {
								throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": For structure must specify an Iterable and a Range!");
							}
							compiledCode.add(new Executable_FOR(tokens[1], line.substring(line.indexOf(tokens[1]) + tokens[1].length() + 1), body));
							break;
						case "Each":
							if( tokens.length < 2 ) {
								throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Each structure must specify an Iterable!");
							}
							compiledCode.add(new Executable_EACH(tokens[1], body));
							break;
						default:
							throw new ScriptCompilationException();
					}
					f = end+1;
				} else {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": no End specified!");
				}
			} else if( tokens[0].equals("Say") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Say Command must specify a message!" );
				}
				compiledCode.add(new Executable_SAY(line.substring(4)));
			} else if( tokens[0].equals("Wait") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Wait Command must specify a time!" );
				}
				compiledCode.add(new Executable_WAIT(line.substring(5)));
			} else if( tokens[0].equals("Nick") ) {
				if( tokens.length < 3 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Nick Command must specify a User and a string!");
				}
				compiledCode.add(new Executable_NICK(tokens[1], line.substring(line.indexOf(tokens[1]) + tokens[1].length() + 1),override));
			}
			else if( tokens[0].equals("Promote") ) {
				if( tokens.length < 3 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Promote Command must specify a User and a Role!" );
				}
				compiledCode.add(new Executable_PROMOTE(tokens[1], tokens[2], override));
			} else if( tokens[0].equals("Demote") ) {
				if( tokens.length < 3 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Demote Command must specify a User and a Role!" );
				}
				compiledCode.add(new Executable_DEMOTE(tokens[1], tokens[2], override));
			} else if( tokens[0].equals("Mute") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Mute Command must specify a User!" );
				}
				compiledCode.add(new Executable_MUTE(tokens[1], override));
			} else if( tokens[0].equals("Unmute") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Unmute Command must specify a User!" );
				}
				compiledCode.add(new Executable_UNMUTE(tokens[1], override));
			} else if( tokens[0].equals("Deafen") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Deafen Command must specify a User!" );
				}
				compiledCode.add(new Executable_DEAFEN(tokens[1], override));
			} else if( tokens[0].equals("Undeafen") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Undeafen Command must specify a User!" );
				}
				compiledCode.add(new Executable_UNDEAFEN(tokens[1], override));
			} else if( tokens[0].equals("Kick") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Kick Command must specify a User!" );
				}
				compiledCode.add(new Executable_KICK(tokens[1], override));
			} else if( tokens[0].equals("Ban") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Ban Command must specify a User!" );
				}
				compiledCode.add(new Executable_BAN(tokens[1], override));
			}
			else {
				throw new ScriptCompilationException(ErrorType.SYNTAX, " Line " + (f+1) + ": unrecognized command!");
			}
		}
		return new Executable_MULTI(compiledCode.toArray(new Executable[compiledCode.size()]));
	}
	
	public static final String resolveFormattedString(MessageEventWrapper mew, Context context, String string) {
		// TODO: actual resolution
		return "";
	}
	
	public static final String resolveStringVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual resolution
		return "";
	}
	
	public static final int resolveIntVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual resolution
		return 0;
	}
	
	public static final IUser resolveUserVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual resolution
		return null;
	}
	
	public static final IRole resolveRoleVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual resolution
		return null;
	}
	
	public static final boolean resolveBooleanVariable(MessageEventWrapper mew, Context context, String variable) {
		// TODO: actual resolution
		return false;
	}
	
	public static final List<String> resolveStringIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<String> stringIterable = new ArrayList<String>();
		// TODO: actual resolution
		return stringIterable;
	}
	
	public static final List<Integer> resolveIntIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<Integer> intIterable = new ArrayList<Integer>();
		// TODO: actual resolution
		return intIterable;
	}
	
	public static final List<IUser> resolveUserIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<IUser> userIterable = new ArrayList<IUser>();
		// TODO: actual resolution
		return userIterable;
	}
	
	public static final List<IRole> resolveRoleIterable(MessageEventWrapper mew, Context context, String iterable) {
		List<IRole> roleIterable = new ArrayList<IRole>();
		// TODO: actual resolution
		return roleIterable;
	}
	
	@SuppressWarnings("serial")
	public static class ScriptCompilationException extends Exception {
		
		private EmbedObject errorEmbed;
		
		public ScriptCompilationException() {
			this(ErrorType.EXECUTION, "Script Compilation Error!");
		}
		
		public ScriptCompilationException(ErrorType errorType) {
			this(errorType, "Script Compilation Error!");
		}
		
		public ScriptCompilationException(String errorMessage) {
			this(ErrorType.EXECUTION, errorMessage);
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
