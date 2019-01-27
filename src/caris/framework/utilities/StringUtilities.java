package caris.framework.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StringUtilities {
	
	@SuppressWarnings("serial")
	public static final HashMap<String, Integer> NUMBER_LOOKUP = new HashMap<String, Integer>() {{
		put("zero", 0);
		put("one", 1);
		put("two", 2);
		put("three", 3);
		put("four", 4);
		put("five", 5);
		put("six", 6);
		put("seven", 7);
		put("eight", 8);
		put("nine", 9);
		put("ten", 10);
		put("eleven", 11);
		put("twelve", 12);
		put("thirteen", 13);
		put("fourteen", 14);
		put("fifteen", 15);
		put("sixteen", 16);
		put("seventeen", 17);
		put("eighteen", 18);
		put("nineteen", 19);
		put("twenty", 20);
		put("thirty", 30);
		put("forty", 40);
		put("fifty", 50);
		put("sixty", 60);
		put("seventy", 70);
		put("eighty", 80);
		put("ninety", 90);
	}};
	
	@SuppressWarnings("serial")
	public static final HashMap<String, Integer> MULTIPLIER_LOOKUP = new HashMap<String, Integer>() {{
		put("hundred", 100);
		put("thousand", 1000);
		put("million", 1000000);
		put("billion", 1000000000);
	}};
	
	public static int wordToNumber(String input) {
		int total = 0;
		int subTotal = 0;
		String lastToken = "";
		input = input.toLowerCase().trim();
		input = input.replace("-", " ");
		input = input.replaceAll("[^a-zA-Z ]", "");
		String[] tokens = input.split("\\s+");
		for( String token : tokens ) {
			if( NUMBER_LOOKUP.keySet().contains(token) ) {
				subTotal += NUMBER_LOOKUP.get(token);
			} else if( MULTIPLIER_LOOKUP.keySet().contains(token) ) {
				if( lastToken.equals("a") ) {
					total += MULTIPLIER_LOOKUP.get(token);
				} else {
					total += subTotal;
					int lesser = total % MULTIPLIER_LOOKUP.get(token);
					total -= lesser;
					total += lesser * MULTIPLIER_LOOKUP.get(token);
					subTotal = 0;
				}
			}
			lastToken = token;
		}
		if( subTotal >= 0 ) {
			total += subTotal;
		}
		if( total <= 0 ) {
			throw new NumberFormatException("The value specified is too large!");
		}
		return total;
	}
	
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
	
	public static String leftJustify(String string, int length) {
		String result = string;
		for( int f=0; f<length-string.length(); f++) {
			result += " ";
		}
		return result;
	}
	
	public static String rightJustify(String string, int length) {
		String result = "";
		for( int f=0; f<length-string.length(); f++) {
			result += " ";
		}
		return result + string;
	}
	
	public static String centerJustify(String string, int length) {
		String result = "";
		for( int f=0; f<(length-string.length())/2 + (length-string.length())%2; f++) {
			result += " ";
		}
		result += string;
		for( int f=0; f<(length-string.length())/2; f++ ) {
			result += " ";
		}
		return result;
	}
}
