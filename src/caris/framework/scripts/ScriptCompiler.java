package caris.framework.scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basehandlers.ScriptModule;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.scripts.Executable.ScriptExecutionException;
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
import sx.blah.discord.handle.obj.Permissions;
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
					f = end;
				} else {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": no End specified!");
				}
			} else if( tokens[0].equals("Say") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Say Command must specify a String message!" );
				}
				compiledCode.add(new Executable_SAY(line.substring(4)));
			} else if( tokens[0].equals("Wait") ) {
				if( tokens.length < 2 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Wait Command must specify a Number wait time!" );
				}
				compiledCode.add(new Executable_WAIT(line.substring(5)));
			} else if( tokens[0].equals("Nick") ) {
				if( tokens.length < 3 ) {
					throw new ScriptCompilationException(ErrorType.SYNTAX, "Line " + (f+1) + ": Nick Command must specify a User and a String name!");
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
				throw new ScriptCompilationException(ErrorType.SYNTAX, " Line " + (f+1) + ": unrecognized command:\"" + line + "\"!");
			}
		}
		return new Executable_MULTI(compiledCode.toArray(new Executable[compiledCode.size()]));
	}
	
	public static final String resolveStringVariable(MessageEventWrapper mew, Context context, String variable) throws ScriptExecutionException {
		if( variable.startsWith("\"") && variable.endsWith("\"") ) {
			if( variable.length() < 3 ) {
				throw new ScriptExecutionException("Can't have an empty String!");
			}
			
			variable = variable.substring(1, variable.length()-1);
			Matcher stringFormat = Pattern.compile("\\$\\[\\$[^\\[\\]]+\\]").matcher(variable);
			Matcher intFormat = Pattern.compile("\\$\\[\\#[^\\[\\]]+\\]").matcher(variable);
			Matcher mentionFormat = Pattern.compile("\\$\\[\\@[^\\[\\]]+\\]").matcher(variable);
			Matcher booleanFormat = Pattern.compile("\\$\\[\\?[^\\[\\]]+\\]").matcher(variable);
						
			while( stringFormat.find() ) {
				String match = stringFormat.group();
				variable = variable.replace(match, resolveStringVariable(mew, context, match.substring(2, match.length()-1)));
			}
			while( intFormat.find() ) {
				String match = intFormat.group();
				variable = variable.replace(match, resolveStringVariable(mew, context, match.substring(2, match.length()-1)));
			}
			while( mentionFormat.find() ) {
				String match = mentionFormat.group();
				variable = variable.replace(match, resolveStringVariable(mew, context, match.substring(2, match.length()-1)));
			}
			while( booleanFormat.find() ) {
				String match = booleanFormat.group();
				variable = variable.replace(match, resolveStringVariable(mew, context, match.substring(2, match.length()-1)));
			}

			return variable;
		} else {
			switch (variable.charAt(0)) {
				case '#':
					return "" + resolveNumberVariable(mew, context, variable);
				case '?':
					return resolveBooleanVariable(mew, context, variable) ? "true" : "false";
				case '@':
					if( variable.charAt(1) == 'R' ) {
						return resolveRoleVariable(mew, context, variable).getName();
					} else {
						IUser user = resolveUserVariable(mew, context, variable);
						return user.getName() + "#" + user.getDiscriminator();
					}
				case '$':
					Matcher tokenMatcher = Pattern.compile("^(?<iterable>\\$[^ ]+)\\{(?<index>.+)\\}$").matcher(variable);
					if( context.hasString(variable) ) {
						return context.getString(variable);
					} else if( tokenMatcher.matches() ) {
						try {
							return resolveStringIterable(mew, context, "." + tokenMatcher.group("iterable")).get(resolveNumberVariable(mew, context, tokenMatcher.group("index"))-1);
						} catch (IndexOutOfBoundsException e) {
							throw new ScriptExecutionException("Couldn't resolve String \"" + variable + "\"!");
						}
					} else if( variable.equals("$Content") ) {
						return mew.getMessage().getContent();
					}
				default:
					throw new ScriptExecutionException();
			}
		}
	}
	
	public static final int resolveNumberVariable(MessageEventWrapper mew, Context context, String variable) throws ScriptExecutionException {
		// Parenthetical Resolution
		int open = variable.lastIndexOf("(");
		if( open == variable.length() - 1 ) {
			throw new ScriptExecutionException("Missing parenthesis in Number expression \"" + variable + "\"!");
		} else if( open != -1 ) {
			int close = variable.substring(open).indexOf(")");
			if( close != -1 ) {
				close += open;
				return resolveNumberVariable(mew, context, (open == 0 ? "" : variable.substring(0, open)) + resolveNumberVariable(mew, context, variable.substring(open+1, close)) + (close == variable.length() - 1 ? "" : variable.substring(close + 1)));
			} else {
				throw new ScriptExecutionException("Missing parenthesis in Number expression \"" + variable + "\"!");
			}
		} else if( variable.indexOf(")") != -1 ) {
			throw new ScriptExecutionException("Missing parenthesis in Number expression \"" + variable + "\"!");
		}
		
		// Arithmetic Resolution
		int index_div = variable.indexOf(" / ");
		int index_mul = variable.indexOf(" * ");
		int index_sub = variable.indexOf(" - ");
		int index_add = variable.indexOf(" + ");
		if( index_div != -1 && index_div + 3 == variable.length() || index_mul != -1 && index_mul + 3 == variable.length() || index_sub != -1 && index_sub + 3 == variable.length() || index_add != -1 && index_add + 3 == variable.length() ) {
			throw new ScriptExecutionException("Missing operand in Number expression \"" + variable + "\"!");
		} else if( index_sub != -1 || index_sub != index_add ) {
			int index = index_sub != -1 && (index_sub > index_add || index_add == -1) ? index_sub : index_add;
			return resolveNumberVariable(mew, context, variable.substring(0, index)) + (index == index_add ? 1 : -1) * resolveNumberVariable(mew, context,variable.substring(index+3));
		} else if( index_div != -1 || index_mul != -1 ) {
			int index = index_div != -1 && (index_div > index_mul || index_mul == -1) ? index_div : index_mul;
			if( index == index_div ) {
				return resolveNumberVariable(mew, context, variable.substring(0, index)) / resolveNumberVariable(mew, context,variable.substring(index+3));
			} else {
				return resolveNumberVariable(mew, context, variable.substring(0, index)) * resolveNumberVariable(mew, context,variable.substring(index+3));
			}
		}
		
		// Substitutive Resolution
		Matcher tokenMatcher = Pattern.compile("^(?<iterable>#[^ ]+)\\{(?<index>.+)\\}$").matcher(variable);
		Matcher lengthMatcher = Pattern.compile("^#(?<iterable>\\.[^ ]+)\\{\\}$").matcher(variable);
		if( context.hasInt(variable) ) {
			return context.getInt(variable);
		} else if( variable.matches("^[-+]?\\d+$") ) {
			return Integer.parseInt(variable);
		} else if( tokenMatcher.matches() ) {
			try {
				return resolveNumberIterable(mew, context, "." + tokenMatcher.group("iterable")).get(resolveNumberVariable(mew, context, tokenMatcher.group("index"))-1);
			} catch (IndexOutOfBoundsException e) {
				throw new ScriptExecutionException("Couldn't resolve int \"" + variable + "\"!");
			}
		} else if( lengthMatcher.matches() ) {
			String iterable = lengthMatcher.group("iterable");
			switch (iterable.charAt(1)) {
				case '$':
					return resolveStringIterable(mew, context, lengthMatcher.group("iterable")).size();
				case '#':
					return resolveNumberIterable(mew, context, lengthMatcher.group("iterable")).size();
				case '@':
					if( iterable.charAt(2) == 'R' ) {
						return resolveRoleIterable(mew, context, lengthMatcher.group("iterable")).size();
					} else {
						return resolveUserIterable(mew, context, lengthMatcher.group("iterable")).size();
					}
				default:
					throw new ScriptExecutionException("Couldn't resolve Number \"" + variable + "\"!");
			}
		}
		
		// Resolution Failure
		throw new ScriptExecutionException("Couldn't resolve Number\"" + variable + "\"!");
	}
	
	public static final IUser resolveUserVariable(MessageEventWrapper mew, Context context, String variable) throws ScriptExecutionException {
		Matcher tokenMatcher = Pattern.compile("^(?<iterable>@[^ ]+)\\{(?<index>.+)\\}$").matcher(variable);
		if( context.hasUser(variable) ) {
			return context.getUser(variable);
		} else if( tokenMatcher.matches() ) {
			try {
				return resolveUserIterable(mew, context, "." + tokenMatcher.group("iterable")).get(resolveNumberVariable(mew, context, tokenMatcher.group("index"))-1);
			} catch (IndexOutOfBoundsException e) {
				throw new ScriptExecutionException("Couldn't resolve User \"" + variable + "\"!");
			}
		} else if( variable.equals("@Author") ) {
			return mew.getAuthor();
		} else if( variable.equals("@Bot") ) {
			return Brain.cli.getOurUser();
		} else {
			throw new ScriptExecutionException("Couldn't resolve User \"" + variable + "\"!");
		}
	}
	
	public static final IRole resolveRoleVariable(MessageEventWrapper mew, Context context, String variable) throws ScriptExecutionException {
		Matcher tokenMatcher = Pattern.compile("^(?<iterable>@[^ ]+)\\{(?<index>.+)\\}$").matcher(variable);
		if( context.hasRole(variable) ) {
			return context.getRole(variable);
		} else if( tokenMatcher.matches() ) {
			try {
				return resolveRoleIterable(mew, context, "." + tokenMatcher.group("iterable")).get(resolveNumberVariable(mew, context, tokenMatcher.group("index"))-1);
			} catch (IndexOutOfBoundsException e) {
				throw new ScriptExecutionException("Couldn't resolve Role \"" + variable + "\"!");
			}
		} else if( variable.startsWith("@") && variable.length() > 1 ) {
			List<IRole> roleMatches = mew.getGuild().getRolesByName(variable.substring(1));
			if( roleMatches.size() > 0 ) {
				return roleMatches.get(0);
			} else {
				throw new ScriptExecutionException("Couldn't resolve Role \"" + variable + "\"!");
			}
		} else {
			throw new ScriptExecutionException("Couldn't resolve Role \"" + variable + "\"!");
		}
	}
	
	public static final boolean resolveBooleanVariable(MessageEventWrapper mew, Context context, String variable) throws ScriptExecutionException {
		// Parenthetical Resolution
		int open = variable.lastIndexOf("(");
		if( open == variable.length() - 1 ) {
			throw new ScriptExecutionException("Missing parenthesis in Boolean expression \"" + variable + "\"!");
		} else if( open != -1 ) {
			int close = variable.substring(open).indexOf(")");
			if( close != -1 ) {
				close += open;
				return resolveBooleanVariable(mew, context, (open == 0 ? "" : variable.substring(0, open)) + resolveBooleanVariable(mew, context, variable.substring(open+1, close)) + (close == variable.length() - 1 ? "" : variable.substring(close + 1)));
			} else {
				throw new ScriptExecutionException("Missing parenthesis in Boolean expression \"" + variable + "\"!");
			}
		} else if( variable.indexOf(")") != -1 ) {
			throw new ScriptExecutionException("Missing parenthesis in Boolean expression \"" + variable + "\"!");
		}
		
		// Comparator Resolution
		int index_and = variable.indexOf(" and ");
		int index_or = variable.indexOf(" or ");
		int index_eq = variable.indexOf(" == ");
		int index_ne = variable.indexOf(" != ");
		int index_lt = variable.indexOf(" < ");
		int index_gt = variable.indexOf(" > ");
		int index_le = variable.indexOf(" <= ");
		int index_ge = variable.indexOf(" >= ");
		if( index_and != -1 && index_and + 3 == variable.length() || index_or != -1 && index_or + 3 == variable.length() ) {
			throw new ScriptExecutionException("Missing operand in Boolean expression \"" + variable + "\"!");
		} else if( index_or != -1 ) {
			return resolveBooleanVariable(mew, context, variable.substring(0, index_or)) || resolveBooleanVariable(mew, context,variable.substring(index_or+3));
		} else if( index_and != -1 ) {
			return resolveBooleanVariable(mew, context, variable.substring(0, index_and)) && resolveBooleanVariable(mew, context,variable.substring(index_and+3));
		} else if( index_eq != -1 || index_ne != -1 || index_lt != -1 || index_gt != -1 || index_le != -1 || index_ge != -1 ) {
			int priority = Collections.max(Arrays.asList( new Integer[] {index_eq, index_ne, index_lt, index_gt, index_le, index_ge}));
			if( priority == index_eq ) {
				return resolveStringVariable(mew, context, variable.substring(0, index_eq)).equalsIgnoreCase(resolveStringVariable(mew, context,variable.substring(index_eq+3)));
			} else if( priority == index_ne ) {
				return !resolveStringVariable(mew, context, variable.substring(0, index_ne)).equalsIgnoreCase(resolveStringVariable(mew, context,variable.substring(index_ne+3)));
			} else if( priority == index_lt ) {
				return resolveNumberVariable(mew, context, variable.substring(0, index_lt)) < resolveNumberVariable(mew, context,variable.substring(index_lt+3));
			} else if( priority == index_gt ) {
				return resolveNumberVariable(mew, context, variable.substring(0, index_gt)) > resolveNumberVariable(mew, context,variable.substring(index_gt+3));
			} else if( priority == index_le ) {
				return resolveNumberVariable(mew, context, variable.substring(0, index_le)) <= resolveNumberVariable(mew, context,variable.substring(index_le+3));
			} else if( priority == index_ge ) {
				return resolveNumberVariable(mew, context, variable.substring(0, index_ge)) <= resolveNumberVariable(mew, context,variable.substring(index_ge+3));				
			}
		}
		
		// Substitutive Resolution
		Matcher containMatcher = Pattern.compile("^\\?'(?<quote>.*)'$").matcher(variable);
		Matcher containExactMatcher = Pattern.compile("^\\?\"(?<quote>.*)\"$").matcher(variable);
		Matcher roleMatcher = Pattern.compile("^\\?(?<role>@.*)$").matcher(variable);
		if( variable.equals("?Developer") ) {
			return MessageHandler.developerAuthor(mew.getAuthor());
		} else if( variable.equals("?Admin") ) {
			return mew.getAuthor().getPermissionsForGuild(mew.getGuild()).contains(Permissions.ADMINISTRATOR);
		} else if( variable.equals("?BotMention") ) {
			return mew.getMessage().getMentions().contains(Brain.cli.getOurUser());
		} else if( variable.equals("?BotName") ) {
			return mew.containsWord(Constants.NAME);
		} else if( containMatcher.matches() ) {
			return mew.containsPhrase(resolveStringVariable(mew, context, "\"" + containMatcher.group("quote") + "\""));
		} else if( containExactMatcher.matches() ) {
			return mew.getMessage().getContent().contains(resolveStringVariable(mew, context, "\"" + containExactMatcher.group("quote") + "\""));
		} else if( roleMatcher.matches() ) {
			return mew.getAuthor().hasRole(resolveRoleVariable(mew, context, roleMatcher.group("role")));
		}
		
		// Resolution Failure
		throw new ScriptExecutionException("Couldn't resolve Boolean \"" + variable + "\"!");
	}
	
	public static final List<String> resolveStringIterable(MessageEventWrapper mew, Context context, String iterable) throws ScriptExecutionException {
		switch (iterable) {
			case ".$Quoted":
				return mew.quotedTokens;
			case ".$Word":
				return mew.tokens;
			default:
				throw new ScriptExecutionException("Couldn't resolve String Iterable \"" + iterable + "\"!");
		}
	}
	
	public static final List<Integer> resolveNumberIterable(MessageEventWrapper mew, Context context, String iterable) throws ScriptExecutionException {
		switch (iterable) {
		case ".#Number":
			return mew.integerTokens;
		default:
			throw new ScriptExecutionException("Couldn't resolve Number Iterable \"" + iterable + "\"!");
		}
	}
	
	public static final List<IUser> resolveUserIterable(MessageEventWrapper mew, Context context, String iterable) throws ScriptExecutionException {
		Matcher rosterMatcher = Pattern.compile("^\\.@Users\\{(?<role>.+)\\}$").matcher(iterable);
		if( rosterMatcher.matches() ) {
			return mew.getGuild().getUsersByRole(resolveRoleVariable(mew, context, rosterMatcher.group("role")));
		} else if( iterable.equals(".@Users") ) {
			return mew.getGuild().getUsers();
		} else if( iterable.equals(".@Mentioned") ) {
			return mew.getMessage().getMentions();
		} else {
			throw new ScriptExecutionException("Couldn't resolve User Iterable \"" + iterable + "\"!");
		}
	}
	
	public static final List<IRole> resolveRoleIterable(MessageEventWrapper mew, Context context, String iterable) throws ScriptExecutionException {
		Matcher rosterMatcher = Pattern.compile("^\\.@Roles\\{(?<user>.+)\\}$").matcher(iterable);
		if( rosterMatcher.matches() ) {
			return resolveUserVariable(mew, context, rosterMatcher.group("user")).getRolesForGuild(mew.getGuild());
		} else if( iterable.equals(".@Roles") ) {
			List<IRole> roles = mew.getGuild().getRoles();
			roles.remove(mew.getGuild().getEveryoneRole());
			return roles;
		} else if( iterable.equals(".@RoleMentioned") ) {
			return mew.getMessage().getRoleMentions();
		} else {
			throw new ScriptExecutionException("Couldn't resolve Role Iterable \"" + iterable + "\"!");
		}
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
