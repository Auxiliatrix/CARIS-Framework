package caris.framework.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import caris.framework.library.Constants;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;

public class MessageEventWrapper extends MessageReceivedEvent {
	
	public String message;
	public ArrayList<String> tokens;
	public ArrayList<String> quotedTokens;
	public ArrayList<Integer> integerTokens;
	public ArrayList<Long> longTokens;
	
	public boolean adminAuthor;
	public boolean developerAuthor;
	public boolean elevatedAuthor;
	
	@SuppressWarnings("unlikely-arg-type")
	public MessageEventWrapper(MessageReceivedEvent messageReceivedEvent) {
		super(messageReceivedEvent.getMessage());
		message = messageReceivedEvent.getMessage().getContent();
		tokens = TokenUtilities.parseTokens(message);
		quotedTokens = TokenUtilities.parseQuoted(message);
		integerTokens = TokenUtilities.parseIntegers(message);
		longTokens = TokenUtilities.parseLongs(message);	
		
		adminAuthor = messageReceivedEvent.getAuthor().getPermissionsForGuild(messageReceivedEvent.getGuild()).contains(Permissions.ADMINISTRATOR);
		developerAuthor = Arrays.asList(Constants.ADMIN_IDS).contains(messageReceivedEvent.getAuthor().getLongID());
		elevatedAuthor = adminAuthor || developerAuthor;
	}
	
	public ArrayList<String> getCapturedTokens(String boundary) {
		return TokenUtilities.parseCaptured(message, boundary);
	}
	
	public ArrayList<String> getCapturedTokens(String open, String close) {
		return TokenUtilities.parseCaptured(message, open, close);
	}
	
	public boolean containsWord(String word) {
		for( String token : tokens ) {
			if( token.equalsIgnoreCase(word) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAnyWords(List<String> words) {
		for( String word : words ) {
			if( containsWord(word) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAnyWords(String...words) {
		return containsAnyWords(Arrays.asList(words));
	}
	
	public boolean containsAllWords(List<String> words) {
		for( String word : words ) {
			if( !containsWord(word) ) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsAllWords(String...words) {
		return containsAllWords(Arrays.asList(words));
	}
	
}
