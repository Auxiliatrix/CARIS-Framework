package caris.modular.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.modular.reactions.NicknameSetReaction;
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent;
import sx.blah.discord.handle.obj.IGuild;

public class NicknameEnforceHandler extends GeneralHandler<NicknameChangedEvent> {

	public NicknameEnforceHandler() {
		super("NicknameEnforce", false);
	}

	@Override
	public Reaction onStartup() {
		MultiReaction enforceNicknames = new MultiReaction();
		for( IGuild guild : Brain.cli.getGuilds() ) {
			for( Long id : Brain.variables.guildIndex.get(guild.getLongID()).userIndex.keySet() ) {
				if( Brain.variables.guildIndex.get(guild.getLongID()).userIndex.get(id).userData.has("nickname-override") ) {
					enforceNicknames.add(new NicknameSetReaction(guild, guild.getUserByID(id), (String) Brain.variables.guildIndex.get(guild.getLongID()).userIndex.get(id).userData.get("nickname-override")));
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
		if( Brain.variables.guildIndex.get(nicknameChangedEvent.getGuild().getLongID()).userIndex.get(nicknameChangedEvent.getUser().getLongID()).userData.has("nickname-override") ) {
			return new NicknameSetReaction(nicknameChangedEvent.getGuild(), nicknameChangedEvent.getUser(), (String) Brain.variables.guildIndex.get(nicknameChangedEvent.getGuild().getLongID()).userIndex.get(nicknameChangedEvent.getUser().getLongID()).userData.get("nickname-override"));
		} else {
			return null;
		}
	}

	@Override
	public String getDescription() {
		return "Automatically sets people's nicknames if a lock exists";
	}
	
}
