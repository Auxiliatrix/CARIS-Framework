package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.handle.obj.IGuild;

public class ChannelCreateReaction extends Reaction {

	public IGuild guild;
	public String category;
	public String name;
	
	public ChannelCreateReaction(IGuild guild, String category, String name) {
		this.guild = guild;
		this.category = category;
		this.name = name;
	}

	@Override
	public void process() {
		if( guild.getCategoriesByName(category).size() == 0 ) {
			guild.createCategory(category);
		}
		guild.createChannel(name).changeCategory(guild.getCategoriesByName(category).get(0));
	}
	
}
