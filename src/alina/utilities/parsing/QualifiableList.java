package alina.utilities.parsing;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class QualifiableList<E> extends ArrayList<E> {
	
	public boolean containsAny(String...strings) {
		return containsAny(false, strings);
	}
	
	public boolean containsAny(boolean caseSensitive, String...strings) {
		for( String string : strings ) {
			if( this.contains(string) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAll(String...strings) {
		return containsAll(false, strings);
	}
	
	public boolean containsAll(boolean caseSensitive, String...strings) {
		for( String string : strings ) {
			if( this.contains(string) ) {
				return false;
			}
		}
		return true;
	}
	
}
