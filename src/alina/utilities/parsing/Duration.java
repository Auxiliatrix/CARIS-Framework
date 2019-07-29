package alina.utilities.parsing;

import java.util.List;

public class Duration {
	
	@SuppressWarnings("serial")
	public static class NumberOutOfBoundsException extends NumberFormatException {}
	
	public static Duration of( String input ) {
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;		
		List<String> tokens = NumberParsing.replaceNumbersWithWords(input);
		int lastIndex = -1;
		for( int f=0; f<tokens.size(); f++ ) {
			if( tokens.get(f).equals("weeks") || tokens.get(f).equals("week") ) {
				long longDays = NumberParsing.wordsToNumber(tokens.subList(lastIndex+1, f)) * 7;
				if( longDays <= Integer.MAX_VALUE ) {
					days += longDays;
				} else {
					throw new NumberOutOfBoundsException();
				}
				lastIndex = f;
			} else if( tokens.get(f).equals("days") || tokens.get(f).equals("day") ) {
				long longDays = NumberParsing.wordsToNumber(tokens.subList(lastIndex+1, f));
				if( longDays + days <= Integer.MAX_VALUE ) {
					days += longDays;
				} else {
					throw new NumberOutOfBoundsException();
				}
				lastIndex = f;
			} else if( tokens.get(f).equals("hours") || tokens.get(f).equals("hour") ) {
				long longHours = NumberParsing.wordsToNumber(tokens.subList(lastIndex+1, f));
				if( longHours <= Integer.MAX_VALUE ) {
					hours += longHours;
				} else {
					throw new NumberOutOfBoundsException();
				}
				lastIndex = f;
			} else if( tokens.get(f).equals("minutes") || tokens.get(f).equals("minute") ) {
				long longMinutes = NumberParsing.wordsToNumber(tokens.subList(lastIndex+1, f));
				if( longMinutes <= Integer.MAX_VALUE ) {
					minutes += longMinutes;
				} else {
					throw new NumberOutOfBoundsException();
				}
			} else if( tokens.get(f).equals("seconds") || tokens.get(f).equals("second") ) {
				long longSeconds = NumberParsing.wordsToNumber(tokens.subList(lastIndex+1, f));
				if( longSeconds <= Integer.MAX_VALUE ) {
					seconds += longSeconds;
				} else {
					throw new NumberOutOfBoundsException();
				}
				lastIndex = f;
			}
		}
		return Duration.of(days, hours, minutes, seconds);
	}
	
	public static Duration of( int days, int hours, int minutes, int seconds ) {
		if( sanitary(days, hours, minutes, seconds) ) {
			return new Duration(days, hours, minutes, seconds);
		} else {
			return null;
		}
	}
	
	private int days;
	private int hours;
	private int minutes;
	private int seconds;
	
	private Duration( int days, int hours, int minutes, int seconds ) {
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		sanitize();
	}

	public int getDays() {
		return days;
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getSecond() {
		return seconds;
	}
	
	public long asSeconds() {
		return ((this.days * 24 + this.hours) * 60 + this.minutes) * 60 + this.seconds;
	}
	
	public long asMili() {
		return asSeconds() * 1000;
	}
	
	public String asString() {
		String representation = "";
		if( days > 0 ) {
			representation += days + " days";
		}
		if( hours > 0 ) {
			if( !representation.isEmpty() ) {
				representation += " ";
			}
			representation += hours + " hours";
		}
		if( minutes > 0 ) {
			if( !representation.isEmpty() ) {
				representation += " ";
			}
			representation += minutes + " minutes";
		}
		if( seconds > 0 ) {
			if( !representation.isEmpty() ) {
				representation += " ";
			}
			representation += seconds + " seconds";
		}
		return representation;
	}
	
	@Override
	public String toString() {
		return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds";
	}
	
	public Duration plus( Duration duration ) {
		return new Duration(this.days + duration.days, this.hours + duration.hours, this.minutes + duration.minutes, this.seconds + duration.seconds);
	}
	
	public Duration minus( Duration duration ) {
		return new Duration(this.days - duration.days, this.hours - duration.hours, this.minutes - duration.minutes, this.seconds - duration.seconds);
	}
	
	private static boolean sanitary(int days, int hours, int minutes, int seconds) {
		minutes += seconds / 60;
		seconds %= 60;
		hours += minutes / 60;
		minutes %= 60;
		days += hours / 24;
		hours %= 24;
		return days >= 0;
	}
	
	private void sanitize() {
		minutes += seconds / 60;
		seconds %= 60;
		hours += minutes / 60;
		minutes %= 60;
		days += hours / 24;
		hours %= 24;
	}
	
}
