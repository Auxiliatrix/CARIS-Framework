package caris.modular.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.modular.reactions.NicknameSetReaction;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent;
import sx.blah.discord.handle.obj.IGuild;

public class NicknameEnforceHandler extends Handler {

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
	public Reaction handle(Event event) {
		if(event instanceof NicknameChangedEvent) {
			NicknameChangedEvent nicknameChangedEvent = (NicknameChangedEvent) event;
			if( Brain.variables.guildIndex.get(nicknameChangedEvent.getGuild().getLongID()).userIndex.get(nicknameChangedEvent.getUser().getLongID()).userData.has("nickname-override") ) {
				return new NicknameSetReaction(nicknameChangedEvent.getGuild(), nicknameChangedEvent.getUser(), (String) Brain.variables.guildIndex.get(nicknameChangedEvent.getGuild().getLongID()).userIndex.get(nicknameChangedEvent.getUser().getLongID()).userData.get("nickname-override"));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String getDescription() {
		return "Automatically sets people's nicknames if a lock exists";
	}
	
}
