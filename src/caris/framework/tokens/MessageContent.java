package caris.framework.tokens;

import sx.blah.discord.api.internal.json.objects.EmbedObject;

public class MessageContent {

	public String content;
	public EmbedObject embed;
	
	public MessageContent(String content) {
		this(content, null);
	}
	
	public MessageContent(EmbedObject embed) {
		this("", embed);
	}
	
	public MessageContent(String content, EmbedObject embed) {
		this.content = content;
		this.embed = embed;
	}
	
}
