package caris.implementation.events;

import alina.utilities.parsing.WordParsing;
import alina.utilities.qualifieds.QualifiedString;
import alina.utilities.qualifieds.QualifiedStringArrayList;
import caris.common.calibration.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.handle.obj.Permissions;

public class MessageEventWrapper extends MessageEvent {
	
	private QualifiedString qualifiedMessage;
	private QualifiedStringArrayList qualifiedWords;
	
	public MessageEventWrapper(MessageEvent event) {
		super(event.getMessage());
		qualifiedMessage = new QualifiedString(getMessage().getContent());
		qualifiedWords = (QualifiedStringArrayList) WordParsing.parseTokens(getMessage().getContent());
	}
	
	public QualifiedString getQualifiedMessage() {
		return qualifiedMessage;
	}
	
	public QualifiedStringArrayList getQualifiedWords() {
		return qualifiedWords;
	}
	
	public final boolean botAuthor() {
		return getAuthor().isBot();
	}
	
	public final boolean developerAuthor() {
		for( Long id : Constants.DEVELOPER_IDS ) {
			if( getAuthor().getLongID() == id ) {
				return true;
			}
		}
		return false;
	}
	
	public final boolean hasPermissions(Permissions[] permissions) {
		boolean result = true;
		for( Permissions requirement : permissions ) {
			result &= getAuthor().getPermissionsForGuild(getGuild()).contains(requirement);
		}
		return result;
	}
	
}
