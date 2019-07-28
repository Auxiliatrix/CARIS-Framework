package alina.utilities.parsing;

import java.util.List;

public class TimeUtilities {
	/*
	public static Duration stringToTime( String input ) {
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		List<String> tokens = StringUtilities.tokenizeAlphaNum(input);
		int lastIndex = -1;
		for( int f=0; f<tokens.size(); f++ ) {
			if( tokens.get(f).equals("weeks") || tokens.get(f).equals("week") ) {
				days = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f)) * 7;
				lastIndex = f;
			}
			if( tokens.get(f).equals("days") || tokens.get(f).equals("day") ) {
				days = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			} else if( tokens.get(f).equals("hours") || tokens.get(f).equals("hour") ) {
				hours = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			} else if( tokens.get(f).equals("minutes") || tokens.get(f).equals("minute") ) {
				minutes = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			} else if( tokens.get(f).equals("seconds") || tokens.get(f).equals("second") ) {
				seconds = StringUtilities.wordToNumber(tokens.subList(lastIndex+1, f));
				lastIndex = f;
			}
		}
		return Duration.of(days, hours, minutes, seconds);
	}
	*/
}
