package caris.framework.utilities;

import java.util.ArrayList;

public class TokenUtilities {
	public static char[] punctuation = new char[] {
			'.',
			',',
			'!',
			'?',
			';',
			'@',
	};
	
	public static ArrayList<String> parseTokens(String line) {
		ArrayList<String> tokens = new ArrayList<String>();
		while( line.contains("  ") ) {
			line = line.replace("  ", " ");
		}
		while( line.contains("“") ) {
			line = line.replace("“", "\"");
		}
		while( line.contains("”") ) {
			line = line.replace("”", "\"");
		}
		line += " ";
		char[] charArray = line.toCharArray();
		String temp = "";
		boolean openQuote = false;
		boolean border = false;
		for( char c : charArray ) {
			if( c == ' ' && !openQuote && temp.length() > 0 || c == '\n' && !openQuote && temp.length() > 0 ) {
				tokens.add(temp);
				temp = "";
				border = false;
			} else if( c == '"' ) {
				if( openQuote ) {
					openQuote = false;
					if( temp.length() > 0 ) {
						tokens.add(temp);
					}
					temp = "";
					border = true;
				} else {
					openQuote = true;
				}
			} else {
				if( !border ) {
					boolean valid = true;
					for( char p : punctuation ) {
						if( c == p ) {
							valid = false;
							break;
						}
					}
					if( valid || openQuote ) {
						temp += c;
					}
				}
				border = false;
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> parseTokens(String line, char[] punctList) {
		ArrayList<String> tokens = new ArrayList<String>();
		line += " ";
		while( line.contains("  ") ) {
			line = line.replace("  ", " ");
		}
		char[] charArray = line.toCharArray();
		String temp = "";
		boolean openQuote = false;
		boolean border = false;
		for( char c : charArray ) {
			if( c == ' ' && !openQuote && temp.length() > 0 ) {
				tokens.add(temp);
				temp = "";
				border = false;
			} else if( c == '"' ) {
				if( openQuote ) {
					openQuote = false;
					if( temp.length() > 0 ) {
						tokens.add(temp);
					}
					temp = "";
					border = true;
				} else {
					openQuote = true;
				}
			} else {
				if( !border ) {
					boolean valid = true;
					for( char p : punctList ) {
						if( c == p ) {
							valid = false;
							break;
						}
					}
					if( valid || openQuote ) {
						temp += c;
					}
				}
				border = false;
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> parseQuoted(String line) {
		while( line.contains("“") ) {
			line = line.replace("“", "\"");
		}
		while( line.contains("”") ) {
			line = line.replace("”", "\"");
		}
		return parseCaptured(line, "\"");
	}
	
	public static ArrayList<String> parseCaptured(String line, String boundary) {
		ArrayList<String> tokens = new ArrayList<String>();
		line += " ";
		while( line.contains(boundary) ) {
			int indexA = line.indexOf(boundary);
			line = line.substring(indexA+boundary.length());
			int indexB = line.indexOf(boundary);
			if( indexB != -1 && indexB != 0 ) {
				String token = line.substring(0, indexB);
				tokens.add(token);
				line = line.substring(indexB+boundary.length());
			} else {
				break;
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> parseCaptured(String line, String open, String close) {
		ArrayList<String> tokens = new ArrayList<String>();
		line += " ";
		while( line.contains(open) ) {
			int indexOpenStart = line.indexOf(open);
			int indexOpenEnd = indexOpenStart + open.length();
			int indexCloseStart = -1;
			int indexCloseEnd = -1;
			if( indexOpenEnd < line.length() ) {
				String tail = line.substring(indexOpenEnd+1);
				indexCloseStart = tail.indexOf(close) + indexOpenEnd+1;
			}
			if( indexCloseStart != -1 ) {
				indexCloseEnd = indexCloseStart + close.length();
				tokens.add(line.substring(indexOpenEnd, indexCloseStart));
				line = (indexCloseEnd == line.length()) ? "" : line.substring(indexCloseEnd+1);
			} else {
				break;
			}
		}
		return tokens;
	}
	
	public static ArrayList<Integer> parseIntegers(String line) {
		ArrayList<Integer> tokens = new ArrayList<Integer>();
		ArrayList<String> stringTokens = parseTokens(line);
		for( String s : stringTokens ) {
			try {
				tokens.add(Integer.parseInt(s));
			} catch(NumberFormatException e) {}
		}
		return tokens;
	}
	
	public static ArrayList<Long> parseLongs(String line) {
		ArrayList<Long> tokens = new ArrayList<Long>();
		ArrayList<String> stringTokens = parseTokens(line);
		for( String s : stringTokens ) {
			try {
				tokens.add(Long.parseLong(s));
			} catch(NumberFormatException e) {}
		}
		return tokens;
	}
	
	public static String[] combineStringArrays(String[]...s) {
		ArrayList<String> joinedArrayList = new ArrayList<String>();
		for( String[] stringArray : s ) {
			for( String string : stringArray ) {
				joinedArrayList.add(string);
			}
		}
		String[] joinedArray = new String[joinedArrayList.size()];
		for( int f=0; f<joinedArrayList.size(); f++ ) {
			joinedArray[f] = joinedArrayList.get(f);
		}
		return joinedArray;
	}
	
}
