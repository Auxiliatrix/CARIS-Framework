package caris.framework.embedbuilders;

import java.util.ArrayList;

import sx.blah.discord.util.EmbedBuilder;

public abstract class Builder {

	protected ArrayList<EmbedBuilder> embeds;
	
	public Builder() {
		embeds = new ArrayList<EmbedBuilder>();
	}
	
	public ArrayList<EmbedBuilder> getEmbeds() {
		embeds = new ArrayList<EmbedBuilder>();
		buildEmbeds();
		return embeds;
	}
	
	protected abstract void buildEmbeds();

}
