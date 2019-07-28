package caris.framework.utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import caris.common.calibration.LoggerSettings;

public class Logger {
	
	private LocalDateTime timeStamp = null;
	
	private boolean verbose = false;
	private boolean override = false;
	
	private String type = "LOG";
	private List<String> origins = new ArrayList<String>();
	
	public Logger() {
		this(null, false, false, "LOG", new ArrayList<String>());
	}
	
	public Logger(LocalDateTime timeStamp, boolean verbose, boolean override, String type, List<String> origins) {
		this.timeStamp = timeStamp;
		this.verbose = verbose;
		this.override = override;
		this.type = type;
		this.origins = origins;
	}
	
	public Logger clone() {
		return new Logger(timeStamp, verbose, override, type, new ArrayList<String> (origins));
	}
	
	public Logger setTimeStamp() {
		this.timeStamp = LocalDateTime.now();
		return this;
	}
	
	public Logger setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
		return this;
	}
	
	public Logger setVerbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}
	
	public Logger setOverride(boolean override) {
		this.override = override;
		return this;
	}
	
	public Logger setType(String type) {
		this.type = type;
		return this;
	}
	
	public Logger addOrigin(String origin) {
		origins.add(origin);
		return this;
	}
	
	public void log(String message) {
		if( verbose && !LoggerSettings.VERBOSE || origins.size() > LoggerSettings.SPECIFICITY && !override ) {
			return;
		}
		
		System.out.println(build(message));
	}
	
	public void report(String message) {
		if( verbose && !LoggerSettings.VERBOSE || origins.size() > LoggerSettings.SPECIFICITY && !override ) {
			return;
		}
		
		System.err.println(build(message));
	}
	
	private String build(String message) {
		if( timeStamp == null ) {
			timeStamp = LocalDateTime.now();
		}
		String logMessage = timeStamp.toLocalTime().toString() + ": ";
		logMessage += "<" + type + "> ";
		for( String origin : origins ) {
			logMessage += "[" + origin + "]";
		}
		logMessage += " " + message;
		return logMessage;
	}
	
}
