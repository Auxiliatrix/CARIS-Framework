package alina.utilities.qualifieds;

import java.util.Arrays;

public class QualifiedString {
	
	private String string;
	
	public QualifiedString(String string) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
	
	public boolean equalsAny(String...tokens) {
		return equalsAny(false, tokens);
	}
	
	public boolean equalsAny(boolean caseSensitive, String...tokens) {
		return equalsAny(caseSensitive, Arrays.asList(tokens));
	}
	
	public boolean equalsAny(Iterable<String> tokens) {
		return equalsAny(false, tokens);
	}
	
	public boolean equalsAny(boolean caseSensitive, Iterable<String> tokens) {
		for( String token : tokens ) {
			if( token.equals(token) || !caseSensitive && token.equalsIgnoreCase(token) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsIgnoreCase(String token) {
		return string.toLowerCase().contains(token.toLowerCase());
	}
	
	public boolean containsAny(String...tokens) {
		return containsAny(false, tokens);
	}
	
	public boolean containsAny(boolean caseSensitive, String...tokens) {
		return containsAny(caseSensitive, Arrays.asList(tokens));
	}
	
	public boolean containsAny(Iterable<String> tokens) {
		return containsAny(false, tokens);
	}
	
	public boolean containsAny(boolean caseSensitive, Iterable<String> tokens) {
		for( String token : tokens ) {
			if( string.contains(token) || !caseSensitive && containsIgnoreCase(token) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAll(String...tokens) {
		return containsAll(false, tokens);
	}
	
	public boolean containsAll(boolean caseSensitive, String...tokens) {
		return containsAll(caseSensitive, Arrays.asList(tokens));
	}
	
	public boolean containsAll(Iterable<String> tokens) {
		return containsAll(false, tokens);
	}
	
	public boolean containsAll(boolean caseSensitive, Iterable<String> tokens) {
		for( String token : tokens ) {
			if( !(string.contains(token) || !caseSensitive && containsIgnoreCase(token) ) ) {
				return false;
			}
		}
		return true;
	}
	
}
