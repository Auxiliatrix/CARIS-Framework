package caris.framework.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.embedbuilders.ModulesBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.interactives.PagedInteractive;
import caris.framework.main.Brain;
import caris.framework.reactions.InteractiveCreateReaction;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.ModuleDisableReaction;
import caris.framework.reactions.ModuleEnableReaction;
import caris.framework.reactions.UpdateGuildReaction;
import caris.framework.utilities.Verifier;
import caris.framework.utilities.Verifier.Verification;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "Modules", root = true)
@Help(
		category = "Developer",
		description = "View, disable, and enable certain modules.",
		usage = {
					Constants.INVOCATION_PREFIX + "Modules",
					Constants.INVOCATION_PREFIX + "Modules (enable | disable) <module>"
				}
	)
public class ModulesHandler extends MessageHandler {
	
	public ModulesHandler() {
		super(Permissions.ADMINISTRATOR);
	}

	@Override
	public Reaction onStartup() {
		MultiReaction startup = new MultiReaction(-1);
		for( Handler h : Brain.modules.values() ) {
			for( IGuild guild : Brain.cli.getGuilds() ) {
				if( Brain.variables.getGuildInfo(guild).guildData.has(h.name + "_disabled") ) {
					startup.add(new ModuleDisableReaction(guild, h));
				} else if( Brain.variables.getGuildInfo(guild).guildData.has(h.name + "_enabled") ) {
					startup.add(new ModuleEnableReaction(guild, h));
				}
			}
		}
		return startup;
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		MultiReaction modules = new MultiReaction(0);
		Verifier EDVerifier = new Verifier("command", "option", "module").withValidaters(null, x -> ((String) x).equalsIgnoreCase("enable") || ((String) x).equalsIgnoreCase("disable"), x -> Brain.modules.keySet().contains(((String) x).toLowerCase()));
		Verification EDVerification = EDVerifier.verify(mew.tokens);
		if( EDVerification.pass ) {
			Handler h = Brain.modules.get(EDVerification.get(2).toLowerCase());
			if( EDVerification.get(1).equalsIgnoreCase("disable") ) {
				if( h.isRoot() ) {
					modules.add(new MessageReaction(mew.getChannel(), ErrorBuilder.getErrorEmbed(ErrorType.ACCESS, "You cannot disable root modules!")));
				} else {
					modules.add(new ModuleDisableReaction(mew.getGuild(), h));
					modules.add(new UpdateGuildReaction(mew.getGuild(), h.name + "_enabled", null, true));
					modules.add(new UpdateGuildReaction(mew.getGuild(), h.name + "_disabled", true, true));
					modules.add(new MessageReaction(mew.getChannel(), "Module disabled!"));
				}
			} else if( EDVerification.get(1).equalsIgnoreCase("enable") ) {
				modules.add(new ModuleEnableReaction(mew.getGuild(), h));
				modules.add(new UpdateGuildReaction(mew.getGuild(), h.name + "_disabled", null, true));
				modules.add(new UpdateGuildReaction(mew.getGuild(), h.name + "_enabled", true, true));
				modules.add(new MessageReaction(mew.getChannel(), "Module Enabled!"));
			}
		} else {
			EmbedObject[] modulePages = ModulesBuilder.getModulesEmbed(mew.getGuild());
			if( modulePages.length == 1 ) {
				modules.add(new MessageReaction(mew.getChannel(), modulePages[0]));
			} else {
				modules.add(new InteractiveCreateReaction(mew.getChannel(), new PagedInteractive(modulePages)));
			}
		}
		return modules;
	}
	
}
