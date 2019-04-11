package caris.framework.handlers;

import org.json.JSONException;
import org.json.JSONObject;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.ScriptModule;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.embedbuilders.ScriptBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.interactives.PagedInteractive;
import caris.framework.main.Brain;
import caris.framework.reactions.ScriptCreateReaction;
import caris.framework.reactions.ScriptDestroyReaction;
import caris.framework.scripts.ScriptCompiler;
import caris.framework.scripts.ScriptCompiler.ScriptCompilationException;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import caris.framework.utilities.Logger;
import caris.framework.utilities.Verifier;
import caris.framework.utilities.Verifier.Verification;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;

@Module(name = "Script")
@Help(
		category = "Admin",
		description = "Allows you to create, modify, and remove custom script.",
		usage = {
					Constants.INVOCATION_PREFIX + "Script",
					Constants.INVOCATION_PREFIX + "Script view <script>",
					Constants.INVOCATION_PREFIX + "Script remove <script>",
					"For information on creating custom scripts, please see the wiki."
				}
	)
public class ScriptHandler extends MessageHandler {

	public ScriptHandler() {
		super();
	}
	
	@Override
	public Reaction onStartup() {
		MultiReaction startupReaction = new MultiReaction();
		try {
			JSONObject scripts = Brain.variables.atomicVariableData.get().getJSONObject("scripts");
			for( Object key : scripts.keySet() ) {
				try {
					try {
						ScriptModule script = ScriptCompiler.compileScript(scripts.getString((String) key), null);
						startupReaction.add(new ScriptCreateReaction(script));
					} catch (ScriptCompilationException sce) {
						Logger.error(sce.getMessage());
					}
				} catch (JSONException parseKeyError) {
					continue;
				}
			}
		} catch (JSONException loadScriptsError) {
			Brain.variables.atomicVariableData.get().put("scripts", new JSONObject());
		}
		for( IGuild guild : Brain.cli.getGuilds() ) {
			try {
				JSONObject scripts = Brain.variables.getGuildInfo(guild).guildData.getJSONObject("scripts");
				for( Object key : scripts.keySet() ) {
					try {
						try {
							ScriptModule script = ScriptCompiler.compileScript(scripts.getString((String) key), guild);
							startupReaction.add(new ScriptCreateReaction(script));
						} catch (ScriptCompilationException sce) {
							Logger.error(sce.getMessage());
						}
					} catch (JSONException parseKeyError) {
						continue;
					}
				}
			} catch (JSONException loadScriptsError) {
				Brain.variables.getGuildInfo(guild).guildData.put("scripts", new JSONObject());
			}
		}
		return startupReaction;
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew) || mew.getMessage().getContent().startsWith("```CARIS\n") && mew.getMessage().getContent().endsWith("\n```");
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		MultiReaction customReaction = new MultiReaction();
		Verifier customVerifier = new Verifier("command", "option", "script name").withValidaters(null, x -> ((String) x).equalsIgnoreCase("view") || ((String) x).equalsIgnoreCase("remove"), x -> Brain.scripts.containsKey((String) x));
		Verification customVerification = customVerifier.verify(mew.tokens);
		if( customVerification.pass ) {
			ScriptModule script = Brain.scripts.get(customVerification.get(2));
			if( customVerification.get(1).equalsIgnoreCase("view") ) {
				customReaction.add(new MessageReaction(mew.getChannel(), ScriptBuilder.getScriptEmbed(script.name, script.getSource(), script.getGuild(), script.getFormattedRequirements())));
			} else if( customVerification.get(1).equalsIgnoreCase("remove") ) {
				if( script.getGuild() != null || mew.developerAuthor ) {
					customReaction.add(new ScriptDestroyReaction(script));
					customReaction.add(new MessageReaction(mew.getChannel(), "Script successfully deleted!"));
				} else {
					customReaction.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorType.ACCESS, "Global scripts can only be edited by developers!")));
				}
			}
		} else if( mew.getMessage().getContent().startsWith("```CARIS\n") && mew.getMessage().getContent().endsWith("\n```") ) {
			try {
				ScriptModule d = ScriptCompiler.compileScript(mew.getMessage().getContent().substring(10, mew.getMessage().getContent().length()-5), mew.getGuild());
				customReaction.add(new ScriptCreateReaction(d));
				customReaction.add(new MessageReaction(mew.getChannel(), "Script successfully created!"));
			} catch (ScriptCompilationException e) {
				customReaction.add(new MessageReaction(mew.getChannel(), e.getErrorEmbed()));
			}
		} else if( mew.tokens.size() == 1 ) {
			EmbedObject[] pages = ScriptBuilder.getScriptListEmbed(mew.getGuild());
			if( pages.length == 1 ) {
				customReaction.add(new MessageReaction(mew.getChannel(), pages[0]));
			} else {
				customReaction.add(new InteractiveCreateReaction(mew.getChannel(), new PagedInteractive(pages)));
			}
		} else {
			customReaction.add(new MessageReaction(mew.getChannel(), customVerification.getErrorEmbed()));
		}
		return customReaction;
	}
	
}
