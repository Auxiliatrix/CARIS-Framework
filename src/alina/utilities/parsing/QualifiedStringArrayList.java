package alina.utilities.parsing;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("serial")
public class QualifiedStringArrayList extends ArrayList<String> {
	
	public boolean containsIgnoreCase(String token) {
		for( String element : this ) {
			if( token.equalsIgnoreCase(element) ) {
				return true;
			}
		}
		return false;
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
			if( this.contains(token) || !caseSensitive && this.containsIgnoreCase(token) ) {
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
			if( !(this.contains(token) || !caseSensitive && this.containsIgnoreCase(token) ) ) {
				return false;
			}
		}
		return true;
	}
	
}
