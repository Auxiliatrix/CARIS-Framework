package caris.framework.utilities;

import caris.framework.calibration.Constants;
import caris.framework.library.GuildInfo;
import caris.framework.main.Brain;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
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

	public static IMessage sendMessage(IChannel channel, String message, EmbedObject embed) {
		return RequestBuffer.request(() -> {
			try {
				int attempts = 0;
				while(attempts < Constants.STUBBORNNESS) {
					attempts++;
					try {
						return channel.sendMessage(message, embed);
					} catch (Exception e) {
						Logger.error("Reconnect failed. Retrying...");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				throw new DiscordException("Ran out of attempts!");
			}
			catch (DiscordException e) {
				Logger.error("Message could not be sent with error: ");
				e.printStackTrace();
				return null;
			}
		}).get();
	}
	
	public static IMessage sendMessage(IChannel channel, String message) {
		return RequestBuffer.request(() -> {
			try {
				int attempts = 0;
				while(attempts < Constants.STUBBORNNESS) {
					attempts++;
					try {
						return channel.sendMessage(message);
					} catch (Exception e) {
						Logger.error("Reconnect failed. Retrying...");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				throw new DiscordException("Ran out of attempts!");
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
				int attempts = 0;
				while(attempts < Constants.STUBBORNNESS) {
					attempts++;
					try {
						return channel.sendMessage(embed);
					} catch (Exception e) {
						Logger.error("Reconnect failed. Retrying...");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				throw new DiscordException("Ran out of attempts!");
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
		try {
			if( Brain.variables.getGuildInfo(guild).specialChannels.get(GuildInfo.SpecialChannel.LOG) != null ) {
				sendMessage( guild.getChannelByID(Brain.variables.getGuildInfo(guild).specialChannels.get(GuildInfo.SpecialChannel.LOG)), message );
			}
		} catch( NullPointerException e ) {
			
		}
	}

}
