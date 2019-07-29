package caris.implementation.modules;

import com.google.api.services.youtube.model.SearchResult;

import caris.framework.main.Brain;
import caris.framework.modules.MessageModule;
import caris.framework.reactions.Reaction;
import caris.framework.utilities.YoutubeAPIUtility;
import caris.implementation.events.MessageEventWrapper;
import caris.implementation.reactions.ReactionMessageSend;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class ModuleYoutube extends MessageModule<MessageReceivedEvent> {

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
		super(MessageReceivedEvent.class);
	}

	@Override
	public boolean preconditionsMet(MessageReceivedEvent event) {
		MessageEventWrapper mew = new MessageEventWrapper(event);
		return mew.getQualifiedWords().containsIgnoreCase(Brain.name) && (mew.getQualifiedWords().containsAny("search", "fetch", "get") && mew.getQualifiedWords().contains("video") || mew.getQualifiedWords().containsAny("youtube")) && !mew.getQualifiedQuotes().isEmpty();
	}

	@Override
	public Reaction process(MessageReceivedEvent event) {
		MessageEventWrapper mew = new MessageEventWrapper(event);
		String query = "";
		for( String quote : mew.getQualifiedQuotes() ) {
			query += quote + " ";
		}
		SearchResult video = YoutubeAPIUtility.search(query);
		return new ReactionMessageSend(mew.getChannel(), "Found it!\nhttps://www.youtube.com/watch?v=" + video.getId().getVideoId());
	}

}
