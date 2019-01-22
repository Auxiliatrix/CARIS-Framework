package caris.framework.utilities;

import java.util.List;

import caris.framework.library.GuildInfo;
import caris.framework.library.Variables;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

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

	public static IMessage sendMessage(IChannel channel, String message) {
		return RequestBuffer.request(() -> {
			try {
				return channel.sendMessage(message);
			}
			catch (DiscordException e) {
				Logger.error("Message could not be sent with error: ");
				e.printStackTrace();
				return null;
			}
		}).get();
	}

	public static void sendMessage( IChannel[] channel, EmbedBuilder embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}

	public static void sendMessage( List<IChannel> channel, EmbedBuilder embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}
	
	public static void sendMessage( List<IChannel> channel, List<EmbedBuilder> embed ) {
		for( IChannel c : channel ) {
			for( EmbedBuilder e : embed ) {
				sendMessage( c, e );
			}
		}
	}

	public static void sendMessage( IChannel[] channel, EmbedObject embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}

	public static void sendMessage( List<IChannel> channel, EmbedObject embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}

	public static void sendMessage( List<IChannel> channels, String message ) {
		for( IChannel c : channels ) {
			sendMessage(c, message);
		}
	}

	public static IMessage sendMessage( IChannel channel, EmbedBuilder embed ) {
		return RequestBuffer.request(() -> {
			try {
				return channel.sendMessage( embed.build() );
			}
			catch (DiscordException e) {
				Logger.error("Message could not be sent with error: ");
				e.printStackTrace();
				return null;
			}
		}).get();
	}

	public static IMessage sendMessage( IChannel channel, EmbedObject embed ) {
		return RequestBuffer.request(() -> {
			try {
				return channel.sendMessage( embed );
			}
			catch (DiscordException e) {
				Logger.error("Message could not be sent with error: ");
				e.printStackTrace();
				return null;
			}
		}).get();
	}

	public static void sendLog( IGuild guild, String message ) {
		// Send the message to the guild's log channel, if it exists
		if( Variables.guildIndex.get(guild).specialChannels.get(GuildInfo.SpecialChannel.LOG) != null ) {
			sendMessage( Variables.guildIndex.get(guild).specialChannels.get(GuildInfo.SpecialChannel.LOG), message );
		}
	}

}
