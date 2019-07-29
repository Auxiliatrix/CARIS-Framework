package alina.utilities.qualifieds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	public List<Integer> getIntegerTokens() {
		List<Integer> integers = new ArrayList<Integer>();
		for( String token : this ) {
			try {
				integers.add(Integer.parseInt(token));
			} catch (NumberFormatException e) {}
		}
		return integers;
	}
	
	public List<Long> getLongTokens() {
		List<Long> longs = new ArrayList<Long>();
		for( String token : this ) {
			try {
				longs.add(Long.parseLong(token));
			} catch (NumberFormatException e) {}
		}
		return longs;
	}
	
}
