package caris.framework.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class MessageEventWrapper extends MessageReceivedEvent {
	
	public String message;
	public List<String> tokens;
	public List<String> quotedTokens;
	public List<Integer> integerTokens;
	public List<Long> longTokens;
		
	public MessageEventWrapper(MessageReceivedEvent mew) {
		super(mew.getMessage());
		message = mew.getMessage().getContent();
		tokens = TokenUtilities.parseTokens(message);
		quotedTokens = TokenUtilities.parseQuoted(message);
		integerTokens = TokenUtilities.parseIntegers(message);
		longTokens = TokenUtilities.parseLongs(message);	
	}
	
	public List<IUser> getAllMentionedUsers() {
		List<IUser> mentioned = new ArrayList<IUser>();
		mentioned.addAll(getMessage().getMentions());
		for( IRole role : getMessage().getRoleMentions() ) {
			for( IUser user : getGuild().getUsersByRole(role) ) {
				if( !mentioned.contains(user) ) {
					mentioned.add(user);
				}
			}
		}
		return mentioned;
	}
	
	public String notQuoted() {
		String remainder = message;
		for( String quoted : quotedTokens ) {
			remainder = remainder.replace("\"" + quoted + "\"", "");
		}
		return remainder;
	}
	
	public List<String> getCapturedTokens(String boundary) {
		return TokenUtilities.parseCaptured(message, boundary);
	}
	
	public List<String> getCapturedTokens(String open, String close) {
		return TokenUtilities.parseCaptured(message, open, close);
	}
	
	public boolean containsPhrase(String phrase) {
		return message.toLowerCase().contains(phrase);
	}
	
	public boolean containsAnyPhrases(List<String> phrases) {
		for( String phrase : phrases ) {
			if( containsPhrase(phrase) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAnyPhrases(String...phrases) {
		return containsAnyPhrases(Arrays.asList(phrases));
	}
	
	public boolean containsAllPhrases(List<String> phrases) {
		for( String phrase : phrases ) {
			if( !containsPhrase(phrase) ) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsAllPhrases(String...phrases) {
		return containsAllWords(Arrays.asList(phrases));
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
