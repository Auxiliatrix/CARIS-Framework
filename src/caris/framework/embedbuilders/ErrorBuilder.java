package caris.framework.embedbuilders;

import java.awt.Color;

import caris.configuration.calibration.Constants;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class ErrorBuilder {
	
	public enum ErrorType {
		EXECUTION {
			@Override
			public String toString() {
				return "Something went wrong!";
			}
		},
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
		PERMISSION {
			@Override
			public String toString() {
				return "Permissions Missing!";
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
		errorBuilder.withAuthorName(ERROR_SYMBOL + StringUtilities.trim(errorType.toString(), Constants.EMBED_AUTHOR_SIZE - 1, true));
		errorBuilder.withDescription(StringUtilities.trim(errorMessage, Constants.EMBED_DESCRIPTION_SIZE, true));
		return errorBuilder.build();
	}
	
	public static EmbedObject getErrorEmbed(String errorType, String errorMessage) {
		errorBuilder.withAuthorName(ERROR_SYMBOL + StringUtilities.trim(errorType, Constants.EMBED_AUTHOR_SIZE - 1, true));
		errorBuilder.withDescription(StringUtilities.trim(errorMessage, Constants.EMBED_DESCRIPTION_SIZE, true));
		return errorBuilder.build();
	}
}
