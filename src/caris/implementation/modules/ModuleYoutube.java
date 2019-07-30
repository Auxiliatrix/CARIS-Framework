package caris.implementation.modules;

import com.google.api.services.youtube.model.SearchResult;

import caris.framework.main.Brain;
import caris.framework.modules.MessageModule;
import caris.framework.reactions.Reaction;
import caris.framework.utilities.YoutubeAPIUtility;
import caris.implementation.events.MessageEventWrapper;
import caris.implementation.reactions.ReactionMessageSend;

public class ModuleYoutube extends MessageModule<MessageEventWrapper> {

	@Override
	public String getDescription() {
		return "Retrieves Youtube video results";
	}

	@Override
	public String getCategory() {
		return "Plugin";
	}

	@Override
	public String getName() {
		return "Youtube";
	}
	
	public ModuleYoutube() {
		super(MessageEventWrapper.class);
	}

	@Override
	public boolean preconditionsMet(MessageEventWrapper event) {
		return event.getQualifiedWords().containsIgnoreCase(Brain.name) && (event.getQualifiedWords().containsAny("search", "fetch", "get") && event.getQualifiedWords().contains("video") || event.getQualifiedWords().containsAny("youtube")) && !event.getQualifiedQuotes().isEmpty();
	}

	@Override
	public Reaction process(MessageEventWrapper event) {
		String query = "";
		for( String quote : event.getQualifiedQuotes() ) {
			query += quote + " ";
		}
		SearchResult video = YoutubeAPIUtility.search(query);
		return new ReactionMessageSend(event.getChannel(), "Found it!\nhttps://www.youtube.com/watch?v=" + video.getId().getVideoId());
	}

}
