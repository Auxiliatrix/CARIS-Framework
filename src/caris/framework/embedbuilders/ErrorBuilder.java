package caris.framework.embedbuilders;

import java.awt.Color;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class ErrorBuilder {
	
	public enum ErrorType {
		SYNTAX {
			@Override
			public String toString() {
				return "Syntax Error!";
			}
		},
		KEYWORD {
			@Override
			public String toString() {
				return "Keyword Error!";
			}
		},
		USAGE {
			@Override
			public String toString() {
				return "Usage Error!";
			}
		},
		ACCESS {
			@Override
			public String toString() {
				return "Access Denied!";
			}
		},
		DEFAULT {
			@Override
			public String toString() {
				return "Operation Failed!";
			}
		},
	}
	
	public static EmbedBuilder errorBuilder = new EmbedBuilder().withColor(Color.RED);
	public static final String ERROR_SYMBOL = "⚠";
	
	public static EmbedObject getErrorEmbed(String errorMessage) {
		return getErrorEmbed(ErrorType.DEFAULT, errorMessage);
	}
	
	public static EmbedObject getErrorEmbed(ErrorType errorType, String errorMessage) {
		errorBuilder.withAuthorName(ERROR_SYMBOL + errorType.toString());
		errorBuilder.withDescription(errorMessage);
		return errorBuilder.build();
	}
	
	public static EmbedObject getErrorEmbed(String errorType, String errorMessage) {
		errorBuilder.withAuthorName(ERROR_SYMBOL + errorType.toString());
		errorBuilder.withDescription(errorMessage);
		return errorBuilder.build();
	}
}
