package caris.framework.handlers;

import caris.framework.basehandlers.Handler.Module;
import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.main.Brain;

@Module(name = "ESTOP", root = true)
@Help(
		category = "Developer", 
		description = "Automatically overrides and kills " + Constants.NAME, 
		usage = {
					Constants.INVOCATION_PREFIX + "ESTOP" + Constants.NAME
				}
	)
public class ESTOPHandler extends MessageHandler {

	public ESTOPHandler() {
		super();
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew) && mew.tokens.get(1).equals(Constants.NAME) && developerAuthor(mew.getAuthor());
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		Brain.cli.logout(); // Doesn't use a reaction because it should happen immediately.\
		return null;
	}

}
