package caris.modular.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class NicknameSetReaction extends Reaction {

	public IGuild guild;
	public IUser user;
	public String nickname;
	
	public NicknameSetReaction(IGuild guild, IUser user, String nickname) {
		this(guild, user, nickname, 1);
	}
	
	public NicknameSetReaction(IGuild guild, IUser user, String nickname, int priority) {
		super(priority);
		this.guild = guild;
		this.user = user;
		this.nickname = nickname;
	}

	@Override
	public void process() {
		guild.setUserNickname(user, nickname);
	}
	
}
