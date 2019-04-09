package caris.fortuna.handlers;

import org.json.JSONObject;

import caris.configuration.calibration.Constants;
import caris.fortuna.embedbuilders.BalanceBuilder;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basehandlers.MessageHandler.Command;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;
import caris.framework.reactions.MessageReaction;
import sx.blah.discord.handle.obj.IUser;

@Module(name = "Balance")
@Help(
		category = "Economy",
		description = "Check yours or another player's balance",
		usage = {
					Constants.INVOCATION_PREFIX + "Balance [@user]",
		}
	)
@Command(aliases = {"Bal"})
public class BalanceHandler extends MessageHandler {

	public BalanceHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		IUser user = mew.getMessage().getMentions().size() > 0 ? mew.getMessage().getMentions().get(0) : mew.getAuthor();
		JSONObject userData = Brain.variables.getUserInfo(mew.getGuild(), user).userData;
		return new MessageReaction(mew.getChannel(), BalanceBuilder.getBalanceEmbed(user.getDisplayName(mew.getGuild()), userData.has("balance") ? userData.getInt("balance") : 0));
	}

}
