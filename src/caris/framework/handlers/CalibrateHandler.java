package caris.framework.handlers;

import caris.configuration.calibration.Constants;
import caris.framework.basehandlers.Handler.Module;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder.Help;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.MessageReaction;
import caris.framework.reactions.UpdateGuildReaction;
import caris.framework.utilities.Verifier;
import caris.framework.utilities.Verifier.ArgClass;
import caris.framework.utilities.Verifier.Verification;
import sx.blah.discord.handle.obj.Permissions;

@Module(name = "Calibrate")
@Help(
		category = "Developer",
		description = "Calibrate various settings for CARIS, such as time zone.",
		usage = {
					Constants.INVOCATION_PREFIX + "Calibrate zone <+-#>"
				}
	)
public class CalibrateHandler extends MessageHandler {

	public CalibrateHandler() {
		super(Permissions.ADMINISTRATOR);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper mew) {
		return invoked(mew);
	}

	@Override
	protected Reaction process(MessageEventWrapper mew) {
		MultiReaction calibrate = new MultiReaction(0);
		Verifier initialVerifier = new Verifier("command", "option", "offset")
										.withArgClasses(ArgClass.STRING, ArgClass.STRING, ArgClass.INTEGER)
										.withValidaters(null, x -> ((String) x).equalsIgnoreCase("zone"), x -> Integer.parseInt((String) x) > -24 && Integer.parseInt((String) x) < 24);
		Verification initialVerification = initialVerifier.verify(mew.tokens);
		if( initialVerification.pass ) {
			if( initialVerification.get(1).equalsIgnoreCase("zone") ) {
				int offset = Integer.parseInt(initialVerification.get(2));
				calibrate.add(new UpdateGuildReaction(mew.getGuild(), "time_zone", offset, true));
				calibrate.add(new MessageReaction(mew.getChannel(), "Time Zone updated successfully!"));
			}
		} else {
			calibrate.add(new MessageReaction(mew.getChannel(), initialVerification.getErrorEmbed()));
		}
		return calibrate;
	}
	
	

}
