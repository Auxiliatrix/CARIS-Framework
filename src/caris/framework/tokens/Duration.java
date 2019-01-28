package caris.framework.tokens;

public class Duration {
	
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
