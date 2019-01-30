package caris.framework.events;

import java.util.Arrays;
import java.util.List;

import caris.framework.calibration.Constants;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;

public class MessageEventWrapper extends MessageReceivedEvent {
	
	public String message;
	public List<String> tokens;
	public List<String> quotedTokens;
	public List<Integer> integerTokens;
	public List<Long> longTokens;
	
	public boolean adminAuthor;
	public boolean developerAuthor;
	public boolean elevatedAuthor;
	
	public MessageEventWrapper(MessageReceivedEvent messageReceivedEvent) {
		super(messageReceivedEvent.getMessage());
		message = messageReceivedEvent.getMessage().getContent();
		tokens = TokenUtilities.parseTokens(message);
		quotedTokens = TokenUtilities.parseQuoted(message);
		integerTokens = TokenUtilities.parseIntegers(message);
		longTokens = TokenUtilities.parseLongs(message);	
		
		adminAuthor = messageReceivedEvent.getAuthor().getPermissionsForGuild(messageReceivedEvent.getGuild()).contains(Permissions.ADMINISTRATOR);
		for( Long id : Constants.ADMIN_IDS ) {
			if( messageReceivedEvent.getAuthor().getLongID() == id ) {
				developerAuthor = true;
			}
		}
		elevatedAuthor = adminAuthor || developerAuthor;
	}
	
	public String notQuoted() {
		String remainder = message;
		for( String quoted : quotedTokens ) {
			remainder.replace("\"" + quoted + "\"", "");
		}
		return remainder;
	}
	
	public List<String> getCapturedTokens(String boundary) {
		return TokenUtilities.parseCaptured(message, boundary);
	}
	
	public List<String> getCapturedTokens(String open, String close) {
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
