package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IMessage;

public class EmbedEditReaction extends Reaction {
	
	public IMessage message;
	public EmbedObject embed;
	
	public EmbedEditReaction(IMessage message, EmbedObject embed) {
		this(message, embed, -1);
	}
	
	public EmbedEditReaction(IMessage message, EmbedObject embed, int priority) {
		super(priority);
		this.message = message;
		this.embed = embed;
	}
	
	@Override
	public void run() {
		message.edit(embed);
	}

}
