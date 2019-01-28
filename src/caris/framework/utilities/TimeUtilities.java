package caris.framework.utilities;

import java.util.List;

import caris.framework.tokens.Duration;

public class TimeUtilities {
	
	public static Duration stringToTime( String input ) {
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		List<String> tokens = StringUtilities.tokenizeAlphaNum(input);
		int lastIndex = -1;
		for( int f=0; f<tokens.size(); f++ ) {
			if( tokens.get(f).equals("days") ) {
				days = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			} else if( tokens.get(f).equals("hours") ) {
				hours = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			} else if( tokens.get(f).equals("minutes") ) {
				minutes = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			} else if( tokens.get(f).equals("seconds") ) {
				seconds = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			}
		}
		return Duration.of(days, hours, minutes, seconds);
	}
	
}
