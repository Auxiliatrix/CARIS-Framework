package caris.implementation.builders;

import java.awt.Color;

import com.google.api.services.youtube.model.SearchResult;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class YoutubeBuilder {

	public static EmbedObject build(SearchResult video) {
		EmbedBuilder youtubeBuilder = new EmbedBuilder();
		youtubeBuilder.withColor(Color.YELLOW);
		youtubeBuilder.withAuthorName(video.getSnippet().getTitle());
		youtubeBuilder.withAuthorUrl("https://www.youtube.com/watch?v=" + video.getId());
		youtubeBuilder.withDescription(video.getSnippet().getDescription());
		return youtubeBuilder.build();
	}
	
}
