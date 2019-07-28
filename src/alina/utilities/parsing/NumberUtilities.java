package alina.utilities.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NumberUtilities {
	
	/*
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println(numberToWord(sc.nextLong()));
		}
	}
	*/
	
	@SuppressWarnings("serial")
	public static final Map<String, Long> MULTIPLIERS = new LinkedHashMap<String, Long>() {{
		put("billion", 1000000000L);
		put("million", 1000000L);
		put("thousand", 1000L);
		put("hundred", 100L);
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, Long> TENS = new LinkedHashMap<String, Long>() {{
		put("ninety", 90L);
		put("eighty", 80L);
		put("seventy", 70L);
		put("sixty", 60L);
		put("fifty", 50L);
		put("forty", 40L);
		put("thirty", 30L);
		put("twenty", 20L);
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, Long> LITERALS = new LinkedHashMap<String, Long>() {{
		put("nineteen", 19L);
		put("eighteen", 18L);
		put("seventeen", 17L);
		put("sixteen", 16L);
		put("fifteen", 15L);
		put("fourteen", 14L);
		put("thirteen", 13L);
		put("twelve", 12L);
		put("eleven", 11L);
		put("ten", 10L);
		put("nine", 9L);
		put("eight", 8L);
		put("seven", 7L);
		put("six", 6L);
		put("five", 5L);
		put("four", 4L);
		put("three", 3L);
		put("two", 2L);
		put("one", 1L);
	}};
	
	public static String numberToWord(long input) {
		if( input < 0 ) {
			return "negative" + numberToWord(Math.abs(input));
		}
		for( Entry<String, Long> entry : MULTIPLIERS.entrySet() ) {
			if( input >= entry.getValue() ) {
				return numberToWord(input / entry.getValue()) + " " + entry.getKey() + " " + numberToWord(input % entry.getValue());
			}
		}
		for( Entry<String, Long> entry : TENS.entrySet() ) {
			if( input >= entry.getValue() ) {
				return entry.getKey() + " " + numberToWord(input - entry.getValue());
			}
		}
		for( Entry<String, Long> entry : LITERALS.entrySet() ) {
			if( input == entry.getValue() ) {
				return entry.getKey();
			}
		}
		return "";
	}

	/*
	public static int wordToNumber(String input) {
		input = input.toLowerCase().trim();
		input = input.replace("-", " ");
		input = input.replaceAll("[^a-zA-Z0-9 ]", "");
		String[] words = input.split("\\s+");
		List<String> tokens = new ArrayList<String>();
		for( String word : words ) {
			if( word.matches("(0|[1-9]\\d*)") ) {
				String conversion = numberToWord(Integer.parseInt(word));
				tokens.addAll(Arrays.asList(conversion.split(" ")));
			} else {
				tokens.add(word);
			}
		}
		return wordToNumber( tokens );
	}
	*/
	/*
	public static int wordToNumber( List<String> tokens ) {
		int total = 0;
		int subTotal = 0;
		String lastToken = "";
		for( String token : tokens ) {
			if( WORD_NUMBER_LOOKUP.keySet().contains(token) ) {
				subTotal += WORD_NUMBER_LOOKUP.get(token);
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
		if( subTotal > 0 ) {
			total += subTotal;
		}
		if( total < 0 ) {
			throw new NumberFormatException("The value specified is too large!");
		}
		if( total == 0 && tokens.get(tokens.size()-1).equals("a") || tokens.get(tokens.size()-1).equals("an") ) {
			total = 1;
		}
		return total;
	}
	*/
	
	public static List<String> tokenizeAlphaNum( String input ) {
		input = input.toLowerCase().trim();
		input = input.replace("-", " ");
		input = input.replaceAll("[^a-zA-Z0-9 ]", "");
		String[] words = input.split("\\s+");
		List<String> tokens = new ArrayList<String>();
		for( String word : words ) {
			if( word.matches("(0|[1-9]\\d*)") ) {
				String conversion = NumberUtilities.numberToWord(Integer.parseInt(word));
				tokens.addAll(Arrays.asList(conversion.split(" ")));
			} else {
				tokens.add(word);
			}
		}
		return tokens;
	}
	
}
