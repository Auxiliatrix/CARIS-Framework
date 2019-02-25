package caris.modular.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.modular.reactions.NicknameSetReaction;
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class NicknameEnforceHandler extends GeneralHandler<NicknameChangedEvent> {

	public NicknameEnforceHandler() {
		super("NicknameEnforce");
	}

	@Override
	public Reaction onStartup() {
		MultiReaction enforceNicknames = new MultiReaction();
		for( IGuild guild : Brain.cli.getGuilds() ) {
			for( IUser user : guild.getUsers() ) {
				if( Brain.variables.getUserInfo(guild, user).userData.has("nickname-override") ) {
					enforceNicknames.add(new NicknameSetReaction(guild, user, (String) Brain.variables.getUserInfo(guild, user).userData.get("nickname-override")));
				}
			}
		}
		return enforceNicknames;
	}
	
	@Override
	public boolean isTriggered(NicknameChangedEvent typedEvent) {
		return true;
	}
	
	@Override
	public Reaction process(NicknameChangedEvent typedEvent) {
		NicknameChangedEvent nicknameChangedEvent = (NicknameChangedEvent) typedEvent;
		if( Brain.variables.getUserInfo(typedEvent.getGuild(), typedEvent.getUser()).userData.has("nickname-override") ) {
			return new NicknameSetReaction(nicknameChangedEvent.getGuild(), nicknameChangedEvent.getUser(), (String) Brain.variables.getUserInfo(typedEvent.getGuild(), typedEvent.getUser()).userData.get("nickname-override"));
		} else {
			return null;
		}
	}

	@Override
	public String getDescription() {
		return "Automatically sets people's nicknames if a lock exists";
	}
	
}
