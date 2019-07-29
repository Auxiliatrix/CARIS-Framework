package alina.utilities.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import caris.common.references.UnicodeReferences;

public class WordParsing {
	
	private static final char[] PUNCTUATION = new char[] {
			'.',
			',',
			'!',
			'?',
			';',
			'@',
			'\'',
	};
	
	public static List<String> removePunctuation(List<String> strings) {
		List<String> removed = new ArrayList<String>();
		for( String string : strings ) {
			removed.add(removePunctuation(string));
		}
		return removed;
	}
	
	public static String removePunctuation(String string) {
		for( char p : PUNCTUATION ) {
			string = string.replace(p, ' ');
		}
		return string;
	}
	
	public static String sanitize(String original) {
		String sanitized = "";
		
		for( char c : original.toCharArray() ) {
			String conversion = Integer.toHexString(c | 0x10000).substring(1);
			if( UnicodeReferences.UNICODE_CONVERSIONS.containsKey(conversion) ) {
				sanitized += UnicodeReferences.UNICODE_CONVERSIONS.get(conversion);
			} else {
				sanitized += c;
			}
		}
		
		sanitized = sanitized.trim().replaceAll("\\s+", " ");
		
		return sanitized;
	}
	
	public static List<String> parseTokens(String line) {
		return parseTokens(line, "\"");
	}
	
	public static List<String> parseTokens(String line, String boundary) {
		List<String> tokens = new ArrayList<String>();
		
		line = sanitize(line);
		
		int quoteIndexOpen = line.indexOf(boundary);
		int quoteIndexClose = (quoteIndexOpen < line.length()-boundary.length()) ? quoteIndexOpen + boundary.length() + line.substring(quoteIndexOpen+boundary.length()).indexOf(boundary) : -1;
		if( quoteIndexOpen == -1 ) {
			tokens.addAll(removePunctuation(Arrays.asList(line.split(" "))));
		} else if( quoteIndexClose == -1 ) {
			tokens.addAll(removePunctuation(Arrays.asList(line.replace(boundary, "").split(" "))));
		} else {
			if( quoteIndexOpen > 0 ) {
				tokens.addAll(parseTokens(line.substring(0, quoteIndexOpen), boundary));
			}
			if( quoteIndexClose - quoteIndexOpen > 0 ) {
				tokens.add(line.substring(quoteIndexOpen+boundary.length(), quoteIndexClose));
			}
			if( quoteIndexClose+boundary.length() < line.length() ) {
				tokens.addAll(parseTokens(line.substring(quoteIndexClose+boundary.length()), boundary));
			}
		}
		
		return tokens;
	}
	
	public static List<String> parseCaptured(String line, String boundary) {
		List<String> tokens = new ArrayList<String>();
		
		line = sanitize(line);
				
		int quoteIndexOpen = line.indexOf(boundary);
		int quoteIndexClose = (quoteIndexOpen < line.length()-boundary.length()) ? quoteIndexOpen + boundary.length() + line.substring(quoteIndexOpen+boundary.length()).indexOf(boundary) : -1;
		if( quoteIndexOpen != -1 && quoteIndexClose != -1 ) {
			if( quoteIndexClose - quoteIndexOpen > 0 ) {
				tokens.add(line.substring(quoteIndexOpen+boundary.length(), quoteIndexClose));
			}
			if( quoteIndexClose+boundary.length() < line.length() ) {
				tokens.addAll(parseCaptured(line.substring(quoteIndexClose+boundary.length()), boundary));
			}
		}
		
		return tokens;
	}
	
	public static List<String> parseCaptured(String line, String open, String close) {
		List<String> tokens = new ArrayList<String>();
		
		line = sanitize(line);
		
		int quoteIndexOpen = line.indexOf(open);
		int quoteIndexClose = (quoteIndexOpen < line.length()-open.length()) ? quoteIndexOpen + open.length() + line.substring(quoteIndexOpen+open.length()).indexOf(close) : -1;
		if( quoteIndexOpen != -1 && quoteIndexClose != -1 ) {
			if( quoteIndexClose - quoteIndexOpen > 0 ) {
				tokens.add(line.substring(quoteIndexOpen+open.length(), quoteIndexClose));
			}
			if( quoteIndexClose+close.length() < line.length() ) {
				tokens.addAll(parseCaptured(line.substring(quoteIndexClose+close.length()), open, close));
			}
		}
		
		return tokens;
	}
	
	public static List<String> parseQuoted(String line) {
		return parseCaptured(line, "\"");
	}
	
	public static String[] combineStringArrays(String[]...arrays) {
		List<String> joinedArrayList = new ArrayList<String>();
		for( String[] array : arrays ) {
			joinedArrayList.addAll(Arrays.asList(array));
		}
		return joinedArrayList.toArray(new String[joinedArrayList.size()]);
	}
	
}
