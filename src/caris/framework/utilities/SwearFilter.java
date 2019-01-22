package caris.framework.utilities;

import java.util.ArrayList;

public class SwearFilter {
	
	public static String[] blacklist = new String[] {
			
	};
	
	public static boolean checkBlacklisted(String line) {
		ArrayList<String> possibilities = generateSubstitutions(compress(substitute(compress((line.length() > 32 ? line.substring(0, 32) : line).toLowerCase()))), 0);
		for( String possibility : possibilities ) {
			for( String word : blacklist ) {
				if( possibility.contains(word) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String compress(String line) {
		line.replace(" ", "");
		char[] array = line.toCharArray();
		String result = array[0] + "";
		char current = array[0];
		for( char c : array ) {
			if( c != current ) {
				result += c;
				current = c;
			}
		}
		return result;
	}
	
	public static String substitute(String line) {
		line = line.replace('2', 's');
		line = line.replace('@', 'a');
		line = line.replace('3', 'e');
		line = line.replace('#', 'h');
		line = line.replace('4', 'a');
		line = line.replace('$', 's');
		line = line.replace('5', 's');
		line = line.replace('%', 'x');
		line = line.replace('7', 't');
		line = line.replace('8', 'b');
		line = line.replace('(', 'c');
		line = line.replace('0', 'o');
		line = line.replace('+', 't');
		line = line.replace('/', 'l');
		line = line.replace('\\', 'l');
		return line;
	}
	
	public static ArrayList<String> generateSubstitutions(String line, int character) {
		ArrayList<String> possibilities = new ArrayList<String>();
		if( character == line.length() ) {
			possibilities.add(compress(line));
			return possibilities;
		} else {
			if( line.charAt(character) == '1' || line.charAt(character) == '!' || line.charAt(character) == '|' ) {
				possibilities.addAll(generateSubstitutions(line.substring(0, character) + "i" + line.substring(character+1),character+1));
				possibilities.addAll(generateSubstitutions(line.substring(0, character) + "l" + line.substring(character+1),character+1));
			} else if( line.charAt(character) == '6' ) {
				possibilities.addAll(generateSubstitutions(line.replaceFirst("6", "b"),character+1));
				possibilities.addAll(generateSubstitutions(line.replaceFirst("6", "g"),character+1));
			} else if( line.charAt(character) == '^' ) {
				possibilities.addAll(generateSubstitutions(line.replaceFirst("\\^", "a"),character+1));
				possibilities.addAll(generateSubstitutions(line.replaceFirst("\\^", "n"),character+1));
			} else if( line.charAt(character) == '&' ) {
				possibilities.addAll(generateSubstitutions(line.replaceFirst("&", "g"),character+1));
				possibilities.addAll(generateSubstitutions(line.replaceFirst("&", "n"),character+1));
			} else if( line.charAt(character) == '*' ) {
				possibilities.addAll(generateSubstitutions(line.replace("*", "a"),character+1));
				possibilities.addAll(generateSubstitutions(line.replace("*", "e"),character+1));
				possibilities.addAll(generateSubstitutions(line.replace("*", "i"),character+1));
				possibilities.addAll(generateSubstitutions(line.replace("*", "o"),character+1));
				possibilities.addAll(generateSubstitutions(line.replace("*", "u"),character+1));
			} else if( line.charAt(character) == '9' ) {
				possibilities.addAll(generateSubstitutions(line.replaceFirst("9", "j"),character+1));
				possibilities.addAll(generateSubstitutions(line.replaceFirst("9", "g"),character+1));
			} else {
				possibilities.addAll(generateSubstitutions(line, character+1));
			}
		}
		return possibilities;
	}
	
}
