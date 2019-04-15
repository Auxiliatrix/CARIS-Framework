package caris.framework.utilities;

import caris.framework.library.GuildInfo;
import caris.framework.main.Brain;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

public class BotUtils {
	
	
	// Actually creates the client object.
	// Magic!
	public static IDiscordClient getBuiltDiscordClient(String token) {
		// Creates a client with the given token.
		// Uses the default value for shards for ease of use
		// Here, any extra params for the bot should be attached.
		// Some examples include: ping timeout, status(invisible, online, ect.)
		return new ClientBuilder()
				.withToken(token)
				.withRecommendedShardCount()
				.build();
	}
	
	public static void sendLog( IGuild guild, String message ) {
		// Send the message to the guild's log channel, if it exists
		try {
			if( Brain.variables.getGuildInfo(guild).specialChannels.get(GuildInfo.SpecialChannel.LOG) != null ) {
				guild.getChannelByID(Brain.variables.getGuildInfo(guild).specialChannels.get(GuildInfo.SpecialChannel.LOG)).sendMessage(message);
			}
		} catch( NullPointerException e ) {
			
		}
	}

}
