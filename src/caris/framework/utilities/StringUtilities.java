package caris.framework.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class StringUtilities {
	
	public static boolean equalsAnyOfIgnoreCase( String a, String...b ) {
		return equalsAnyOfIgnoreCase(a, Arrays.asList(b));
	}
	
	public static boolean equalsAnyOfIgnoreCase( String a, List<String> b ) {
		for( String token : b ) {
			if( a.equalsIgnoreCase(token) ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasIgnoreCase(String[] a, String b) {
		return hasIgnoreCase(Arrays.asList(a), b);
	}
	
	public static boolean hasIgnoreCase(List<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasIgnoreCase(Set<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasAnyOfIgnoreCase(String[] a, String... b) {
		return hasAnyOfIgnoreCase(Arrays.asList(a), b);
	}
	
	public static boolean hasAnyOfIgnoreCase(String[] a, List<String> b) {
		return hasAnyOfIgnoreCase(Arrays.asList(a), b);
	}
	
	public static boolean hasAnyOfIgnoreCase(List<String> a, String... b) {
		return hasAnyOfIgnoreCase(a, Arrays.asList(b));
	}
	
	public static boolean hasAnyOfIgnoreCase(List<String> a, List<String> b) {
		for( String token : b ) {
			if( hasIgnoreCase(a, token) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasAllOfIgnoreCase(String[] a, String... b) {
		return hasAllOfIgnoreCase(Arrays.asList(a), b);
	}
	
	public static boolean hasAllOfIgnoreCase(String[] a, List<String> b) {
		return hasAllOfIgnoreCase(Arrays.asList(a), b);
	}
	
	public static boolean hasAllOfIgnoreCase(List<String> a, String... b) {
		return hasAllOfIgnoreCase(a, Arrays.asList(b));
	}
	
	public static boolean hasAllOfIgnoreCase(List<String> a, List<String> b) {
		for( String token : b ) {
			if( !hasIgnoreCase(a, token) ) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean containsIgnoreCase(String a, String b) {
		return a.toLowerCase().contains(b.toLowerCase());
	}

	public static boolean containsIgnoreCase(String[] a, String b) {
		return containsIgnoreCase(Arrays.asList(a), b);
	}
	
	public static boolean containsIgnoreCase(List<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
	public static Boolean containsIgnoreCase(Set<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsAnyOfIgnoreCase(String a, String... b) {
		return containsAnyOfIgnoreCase(a, Arrays.asList(b));
	}
	
	public static boolean containsAnyOfIgnoreCase(String a, List<String> b) {
		for( String token : b ) {
			if( containsIgnoreCase(a, token) ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsAllOfIgnoreCase(String a, String... b) {
		return containsAllOfIgnoreCase(a, Arrays.asList(b));
	}

	public static boolean containsAllOfIgnoreCase(String a, List<String> b) {
		for( String token : b ) {
			if( !containsIgnoreCase(a, token) ) {
				return false;
			}
		}
		return true;
	}
}
