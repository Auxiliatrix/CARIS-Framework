package alina.utilities.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import caris.common.references.UnicodeReferences;

public class TokenUtilities {

	private static final char[] PUNCTUATION = new char[] {
			'.',
			',',
			'!',
			'?',
			';',
			'@',
			'\'',
	};
	
	public static String sanitize(String original) {
		String sanitized = "";
		
		for( char p : PUNCTUATION ) {
			original = original.replace(p, ' ');
		}
		
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
		List<String> tokens = new ArrayList<String>();
		
		line = sanitize(line);
		
		int quoteIndexOpen = line.indexOf("\"");
		int quoteIndexClose = (quoteIndexOpen < line.length()-1) ? line.substring(quoteIndexOpen+1).indexOf("\"") : -1;
		if( quoteIndexOpen == -1 ) {
			tokens.addAll(Arrays.asList(line.split(" ")));
		} else if( quoteIndexClose == -1 ) {
			tokens.addAll(Arrays.asList(line.replace("\"", "").split(" ")));
		} else {
			if( quoteIndexOpen > 0 ) {
				tokens.addAll(parseTokens(line.substring(0, quoteIndexOpen)));
			}
			if( quoteIndexClose - quoteIndexOpen > 0 ) {
				tokens.add(line.substring(quoteIndexOpen+1, quoteIndexClose));
			}
			if( quoteIndexClose < line.length() ) {
				tokens.addAll(parseTokens(line.substring(quoteIndexClose+1)));
			}
		}
		
		return tokens;
	}
	
	public static List<String> parseTokens(String line, String boundary) {
		List<String> tokens = new ArrayList<String>();
		
		line = sanitize(line);
		
		int quoteIndexOpen = line.indexOf(boundary);
		int quoteIndexClose = (quoteIndexOpen < line.length()-boundary.length()) ? line.substring(quoteIndexOpen+boundary.length()).indexOf(boundary) : -1;
		if( quoteIndexOpen == -1 ) {
			tokens.addAll(Arrays.asList(line.split(" ")));
		} else if( quoteIndexClose == -1 ) {
			tokens.addAll(Arrays.asList(line.replace(boundary, "").split(" ")));
		} else {
			if( quoteIndexOpen > 0 ) {
				tokens.addAll(parseTokens(line.substring(0, quoteIndexOpen)));
			}
			if( quoteIndexClose - quoteIndexOpen > 0 ) {
				tokens.add(line.substring(quoteIndexOpen+boundary.length(), quoteIndexClose));
			}
			if( quoteIndexClose+boundary.length() < line.length() ) {
				tokens.addAll(parseTokens(line.substring(quoteIndexClose+boundary.length())));
			}
		}
		
		return tokens;
	}
	
	public static List<String> parseCaptured(String line, String boundary) {
		List<String> tokens = new ArrayList<String>();
		
		line = sanitize(line);
		
		int quoteIndexOpen = line.indexOf(boundary);
		int quoteIndexClose = (quoteIndexOpen < line.length()-boundary.length()) ? line.substring(quoteIndexOpen+boundary.length()).indexOf(boundary) : -1;
		if( quoteIndexOpen != -1 && quoteIndexClose != -1 ) {
			if( quoteIndexClose - quoteIndexOpen > 0 ) {
				tokens.add(line.substring(quoteIndexOpen+boundary.length(), quoteIndexClose));
			}
			if( quoteIndexClose+boundary.length() < line.length() ) {
				tokens.addAll(parseTokens(line.substring(quoteIndexClose+boundary.length())));
			}
		}
		
		return tokens;
	}
	
	public static List<String> parseCaptured(String line, String open, String close) {
		List<String> tokens = new ArrayList<String>();
		
		line = sanitize(line);
		
		int quoteIndexOpen = line.indexOf(open);
		int quoteIndexClose = (quoteIndexOpen < line.length()-open.length()) ? line.substring(quoteIndexOpen+open.length()).indexOf(close) : -1;
		if( quoteIndexOpen != -1 && quoteIndexClose != -1 ) {
			if( quoteIndexClose - quoteIndexOpen > 0 ) {
				tokens.add(line.substring(quoteIndexOpen+open.length(), quoteIndexClose));
			}
			if( quoteIndexClose+close.length() < line.length() ) {
				tokens.addAll(parseTokens(line.substring(quoteIndexClose+close.length())));
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
