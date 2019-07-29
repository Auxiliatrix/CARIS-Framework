package alina.utilities.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NumberParsing {
	
	@SuppressWarnings("serial")
	public static final Map<String, Integer> MULTIPLIERS = new LinkedHashMap<String, Integer>() {{
		put("billion", 1000000000);
		put("million", 1000000);
		put("thousand", 1000);
		put("hundred", 100);
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, Integer> TENS = new LinkedHashMap<String, Integer>() {{
		put("ninety", 90);
		put("eighty", 80);
		put("seventy", 70);
		put("sixty", 60);
		put("fifty", 50);
		put("forty", 40);
		put("thirty", 30);
		put("twenty", 20);
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, Integer> LITERALS = new LinkedHashMap<String, Integer>() {{
		put("nineteen", 19);
		put("eighteen", 18);
		put("seventeen", 17);
		put("sixteen", 16);
		put("fifteen", 15);
		put("fourteen", 14);
		put("thirteen", 13);
		put("twelve", 12);
		put("eleven", 11);
		put("ten", 10);
		put("nine", 9);
		put("eight", 8);
		put("seven", 7);
		put("six", 6);
		put("five", 5);
		put("four", 4);
		put("three", 3);
		put("two", 2);
		put("one", 1);
	}};
	
	public static String numberToWords(long input) {
		if( input < 0 ) {
			return "negative" + numberToWords(Math.abs(input));
		}
		for( Entry<String, Integer> entry : MULTIPLIERS.entrySet() ) {
			if( input >= entry.getValue() ) {
				return numberToWords(input / entry.getValue()) + " " + entry.getKey() + " " + numberToWords(input % entry.getValue());
			}
		}
		for( Entry<String, Integer> entry : TENS.entrySet() ) {
			if( input >= entry.getValue() ) {
				return entry.getKey() + " " + numberToWords(input - entry.getValue());
			}
		}
		for( Entry<String, Integer> entry : LITERALS.entrySet() ) {
			if( input == entry.getValue() ) {
				return entry.getKey();
			}
		}
		return "";
	}

	public static long wordsToNumber( String input ) {
		return wordsToNumber(replaceNumbersWithWords(input));
	}
	
	public static long wordsToNumber( List<String> tokens ) {
		long total = 0;
		long subTotal = 0;
		String lastToken = "";
		for( String token : tokens ) {
			if( LITERALS.keySet().contains(token) ) {
				subTotal += LITERALS.get(token);
			} else if( MULTIPLIERS.keySet().contains(token) ) {
				if( lastToken.equals("a") ) {
					total += MULTIPLIERS.get(token);
				} else {
					total += subTotal;
					long lesser = total % MULTIPLIERS.get(token);
					total -= lesser;
					total += lesser * MULTIPLIERS.get(token);
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
	
	public static List<String> replaceNumbersWithWords( String input ) {
		input = input.toLowerCase().trim();
		input = input.replace("-", " ");
		input = input.replaceAll("[^a-zA-Z0-9 ]", "");
		String[] words = input.split("\\s+");
		List<String> tokens = new ArrayList<String>();
		for( String word : words ) {
			if( word.matches("(0|[1-9]\\d*)") ) {
				String conversion = numberToWords(Integer.parseInt(word));
				tokens.addAll(Arrays.asList(conversion.split(" ")));
			} else {
				tokens.add(word);
			}
		}
		return tokens;
	}
	
}
