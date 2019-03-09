package caris.configuration.library;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.Permissions;

public class PermissionsString {

	@SuppressWarnings("serial")
	public static final Map<Permissions, String> PERMISSIONS_STRING = new HashMap<Permissions, String>() {{
		put(Permissions.ADD_REACTIONS, "RXTN");
		put(Permissions.ADMINISTRATOR, "ADMIN");
		put(Permissions.ATTACH_FILES, "+FILE");
		put(Permissions.BAN, "BAN");
		put(Permissions.CHANGE_NICKNAME, "CHNG_NCK");
		put(Permissions.CREATE_INVITE, "INVITE");
		put(Permissions.EMBED_LINKS, "+LINK");
		put(Permissions.KICK, "KICK");
		put(Permissions.MANAGE_CHANNEL, "MNG_CHNL");
		put(Permissions.MANAGE_CHANNELS, "MNG_CHNLS");
		put(Permissions.MANAGE_EMOJIS, "MNG_MOJS");
		put(Permissions.MANAGE_MESSAGES, "MNG_MSGS");
		put(Permissions.MANAGE_NICKNAMES, "MNG_NCKS");
		put(Permissions.MANAGE_PERMISSIONS, "MNG_PRMS");
		put(Permissions.MANAGE_ROLES, "MNG_ROLE");
		put(Permissions.MANAGE_SERVER, "MNG_SRVR");
		put(Permissions.MANAGE_WEBHOOKS, "WEBHOOK");
		put(Permissions.MENTION_EVERYONE, "MNTN_ALL");
		put(Permissions.READ_MESSAGE_HISTORY, "READ_HIST");
		put(Permissions.READ_MESSAGES, "READ_MSGS");
		put(Permissions.SEND_MESSAGES, "SND_MSGS");
		put(Permissions.SEND_TTS_MESSAGES, "SND_TTS");
		put(Permissions.USE_EXTERNAL_EMOJIS, "EXT_MOJS");
		put(Permissions.VIEW_AUDIT_LOG, "VIEW_LOG");
		put(Permissions.VOICE_CONNECT, "VC_CNCT");
		put(Permissions.VOICE_DEAFEN_MEMBERS, "VC_DEAF");
		put(Permissions.VOICE_MOVE_MEMBERS, "VC_MOVE");
		put(Permissions.VOICE_MUTE_MEMBERS, "VC_MUTE");
		put(Permissions.VOICE_SPEAK, "VC_TALK");
		put(Permissions.VOICE_USE_VAD, "VC_VAD");
	}};
	
}
