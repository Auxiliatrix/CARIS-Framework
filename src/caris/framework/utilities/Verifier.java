package caris.framework.utilities;

import java.util.List;

import caris.framework.embedbuilders.ErrorBuilder;
import caris.framework.embedbuilders.ErrorBuilder.ErrorType;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

public class Verifier {

	public enum ArgClass {
		STRING,
		INTEGER
	}

	private class ValidationToken {
		public String variableName;
		public ArgClass argClass;
		@SuppressWarnings("rawtypes")
		public Validater validater;
		
		public ValidationToken(String variableName) {
			this.variableName = variableName;
			this.argClass = null;
			this.validater = null;
		}
		
		@SuppressWarnings("unchecked")
		public boolean validate(String s) {
			if( argClass != null ) {
				if( argClass == ArgClass.INTEGER ) {
					try {
						Integer.parseInt(s);
					} catch (NumberFormatException e) {
						return false;
					}
				}
			}
			if( validater != null ) {
				return validater.validate(s);
			}
			return true;
		}
	}
	
	private ValidationToken[] validationTokens;
	
	public Verifier(String...variableNames) {
		validationTokens = new ValidationToken[variableNames.length];
		for( int f=0; f<variableNames.length; f++ ) {
			validationTokens[f] = new ValidationToken(variableNames[f]);
		}
	}
	
	public Verifier withArgClasses(ArgClass...argClasses) {
		for( int f=0; f<Math.min(validationTokens.length, argClasses.length); f++ ) {
			if( argClasses[f] != null ) {
				validationTokens[f].argClass = argClasses[f];
			}
		}
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public Verifier withValidaters(Validater...validaters) {
		for( int f=0; f<Math.min(validationTokens.length, validaters.length); f++ ) {
			if( validaters[f] != null ) {
				validationTokens[f].validater = validaters[f];
			}
		}
		return this;
	}
	
	public Verification verify(List<String> tokens) {
		for( int f=0; f<validationTokens.length; f++ ) {
			if( tokens.size() < f+1 ) {
				return new Verification(false, tokens, validationTokens[f].variableName, ErrorType.SYNTAX);
			}
			if( !validationTokens[f].validate(tokens.get(f)) ) {
				return new Verification(false, tokens, validationTokens[f].variableName, ErrorType.KEYWORD);
			}
		}
		return new Verification(true, tokens);
	}

	public interface Validater<T> {
		public boolean validate(T t);
	}
	
	public class Verification {
		
		public boolean pass;
		public String failName;
		public ErrorType error;
		
		private List<String> matches;
		
		public Verification(boolean pass, List<String> matches) {
			this(pass, matches, null, null);
		}
		
		public Verification(boolean pass, List<String> matches, String failName, ErrorType error) {
			this.pass = pass;
			this.matches = matches;
			this.failName = failName;
			this.error = error;
		}
		
		public String getErrorMessage() {
			if( pass ) {
				return null;
			} else {
				String message = "";
				message += "You must specify a ";
				if( error == ErrorType.KEYWORD ) {
					message += "valid ";
				}
				message += failName;
				return message + "!";
			}
		}
		
		public EmbedObject getErrorEmbed() {
			return ErrorBuilder.getErrorEmbed(error, getErrorMessage());
		}
		
		public String get(int index) {
			return matches.get(index);
		}
		
		public int size() {
			return matches.size();
		}
		
	}
	
}
