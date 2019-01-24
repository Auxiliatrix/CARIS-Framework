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
		CONTEXT {
			@Override
			public String toString() {
				return "Context Error!";
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
	public static final String ERROR_SYMBOL = ":no_entry_sign:";
	
	public EmbedObject getErrorEmbed(String errorMessage) {
		return getErrorEmbed(ErrorType.DEFAULT, errorMessage);
	}
	
	public EmbedObject getErrorEmbed(ErrorType errorType, String errorMessage) {
		errorBuilder.withAuthorName(ERROR_SYMBOL + errorType.toString());
		errorBuilder.withDescription(errorMessage);
		return errorBuilder.build();
	}
	
	public EmbedObject getErrorEmbed(String errorType, String errorMessage) {
		errorBuilder.withAuthorName(ERROR_SYMBOL + errorType.toString());
		errorBuilder.withDescription(errorMessage);
		return errorBuilder.build();
	}
}
