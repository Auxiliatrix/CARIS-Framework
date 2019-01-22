package caris.framework.events;

import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;

import sx.blah.discord.handle.impl.events.guild.*;
import sx.blah.discord.handle.impl.events.guild.category.*;
import sx.blah.discord.handle.impl.events.guild.channel.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.*;
import sx.blah.discord.handle.impl.events.guild.channel.webhook.*;
import sx.blah.discord.handle.impl.events.guild.member.*;
import sx.blah.discord.handle.impl.events.guild.role.*;
import sx.blah.discord.handle.impl.events.guild.voice.*;
import sx.blah.discord.handle.impl.events.guild.voice.user.*;
import sx.blah.discord.handle.impl.events.module.*;
import sx.blah.discord.handle.impl.events.shard.*;
import sx.blah.discord.handle.impl.events.user.*;

import sx.blah.discord.util.audio.events.*;

public class SuperEvent {
	// A catch all class for events.
	
	// The One True Catch-All
	@EventSubscriber public void onEvent( Event event ) {}
	
    // Guild Events
	@EventSubscriber public void onAllUsersReceived( AllUsersReceivedEvent event ) {}
	@EventSubscriber public void onGuildCreate( GuildCreateEvent event ) {}
	@EventSubscriber public void onGuildEmojisUpdate( GuildEmojisUpdateEvent event ) {}
	@EventSubscriber public void onGuild( GuildEvent event ) {}
	@EventSubscriber public void onGuildLeave( GuildLeaveEvent event ) {}
	@EventSubscriber public void onGuildTransferOwnership( GuildTransferOwnershipEvent event ) {}
	@EventSubscriber public void onGuildUnavailable( GuildUnavailableEvent event ) {}
	@EventSubscriber public void onGuildUpdate( GuildUpdateEvent event ) {}
	
	// Guild Category Events
	@EventSubscriber public void onCategoryCreate( CategoryCreateEvent event ) {}
	@EventSubscriber public void onCategoryDelete( CategoryDeleteEvent event ) {}
	@EventSubscriber public void onCategory( CategoryEvent event ) {}
	@EventSubscriber public void onCategoryUpdate( CategoryUpdateEvent event ) {}

	// Guild Channel Events
	@EventSubscriber public void onChannelCategoryUpdate( ChannelCategoryUpdateEvent event ) {}
	@EventSubscriber public void onChannelCreate( ChannelCreateEvent event ) {}
	@EventSubscriber public void onChannelDelete( ChannelDeleteEvent event ) {}
	@EventSubscriber public void onChannel( ChannelEvent event ) {}
	@EventSubscriber public void onChannelUpdate( ChannelUpdateEvent event ) {}
	@EventSubscriber public void onTyping( TypingEvent event ) {}

	//Guild Channel Message Events
	@EventSubscriber public void onMention( MentionEvent event ) {}
	@EventSubscriber public void onMessageDelete( MessageDeleteEvent event ) {}
	//@EventSubscriber public void onMessageEdit( MessageEditEvent event ) {}
	@EventSubscriber public void onMessageEmbed( MessageEmbedEvent event ) {}
	@EventSubscriber public void onMessage( MessageEvent event ) {}
	@EventSubscriber public void onMessagePin( MessagePinEvent event ) {}
	@EventSubscriber public void onMessageReceived( MessageReceivedEvent event ) {}
	@EventSubscriber public void onMessageSend( MessageSendEvent event ) {}
	@EventSubscriber public void onMessageUnpin( MessageUnpinEvent event ) {}
	@EventSubscriber public void onMessageUpdate( MessageUpdateEvent event ) {}

	// Guild Channel Reaction Events
	@EventSubscriber public void onReactionAdd( ReactionAddEvent event ) {}
	@EventSubscriber public void onReaction( ReactionEvent event ) {}
	@EventSubscriber public void onReactionRemove( ReactionRemoveEvent event ) {}

	// Guild Channel Webhook Events
	@EventSubscriber public void onWebhookCreate( WebhookCreateEvent event ) {}
	@EventSubscriber public void onWebhookDelete( WebhookDeleteEvent event ) {}
	@EventSubscriber public void onWebhook( WebhookEvent event ) {}
	@EventSubscriber public void onWebhookUpdate( WebhookUpdateEvent event ) {}

	// Guild Member Events
	@EventSubscriber public void onGuildMember( GuildMemberEvent event ) {}
	@EventSubscriber public void onNicknameChanged( NicknameChangedEvent event ) {}
	@EventSubscriber public void onUserBan( UserBanEvent event ) {}
	@EventSubscriber public void onUserJoin( UserJoinEvent event ) {}
	@EventSubscriber public void onUserLeave( UserLeaveEvent event ) {}
	@EventSubscriber public void onUserPardon( UserPardonEvent event ) {}
	@EventSubscriber public void onUserRoleUpdate( UserRoleUpdateEvent event ) {}

	// Guild Role Events
	@EventSubscriber public void onRoleCreate( RoleCreateEvent event ) {}
	@EventSubscriber public void onRoleDelete( RoleDeleteEvent event ) {}
	@EventSubscriber public void onRole( RoleEvent event ) {}
	@EventSubscriber public void onRoleUpdate( RoleUpdateEvent event ) {}

	// Guild Voice Events
	@EventSubscriber public void onVoiceChannelCreate( VoiceChannelCreateEvent event ) {}
	@EventSubscriber public void onVoiceChannelDelete( VoiceChannelDeleteEvent event ) {}
	@EventSubscriber public void onVoiceChannel( VoiceChannelEvent event ) {}
	@EventSubscriber public void onVoiceChannelUpdate( VoiceChannelUpdateEvent event ) {}
	@EventSubscriber public void onVoiceDisconnected( VoiceDisconnectedEvent event ) {}
	@EventSubscriber public void onVoicePing( VoicePingEvent event ) {}

	// Guild Voice User Events
	@EventSubscriber public void onUserSpeaking( UserSpeakingEvent event ) {}
	@EventSubscriber public void onUserVoiceChannel( UserVoiceChannelEvent event ) {}
	@EventSubscriber public void onUserVoiceChannelJoin( UserVoiceChannelJoinEvent event ) {}
	@EventSubscriber public void onUserVoiceChannelLeave( UserVoiceChannelLeaveEvent event ) {}
	@EventSubscriber public void onUserVoiceChannelMove( UserVoiceChannelMoveEvent event ) {}

	// Module Events
	@EventSubscriber public void onModuleDisabled( ModuleDisabledEvent event ) {}
	@EventSubscriber public void onModuleEnabled( ModuleEnabledEvent event ) {}
	@EventSubscriber public void onModule( ModuleEvent event ) {}
	
	// Shard Events
	@EventSubscriber public void onDisconnected( DisconnectedEvent event ) {}
	@EventSubscriber public void onLogin( LoginEvent event ) {}
	@EventSubscriber public void onReconnectFailure( ReconnectFailureEvent event ) {}
	@EventSubscriber public void onReconnectSuccess( ReconnectSuccessEvent event ) {}
	@EventSubscriber public void onResumed( ResumedEvent event ) {}
	@EventSubscriber public void onShard( ShardEvent event ) {}
	@EventSubscriber public void onShardReady( ShardReadyEvent event ) {}
	
	// User Events
	@EventSubscriber public void onPresenceUpdate( PresenceUpdateEvent event ) {}
	@EventSubscriber public void onUser( UserEvent event ) {}
	@EventSubscriber public void onUserUpdate( UserUpdateEvent event ) {}
	
	// Audio Events
	@EventSubscriber public void onAudioPlayerClean( AudioPlayerCleanEvent event ) {}
	@EventSubscriber public void onAudioPlayer( AudioPlayerEvent event ) {}
	@EventSubscriber public void onAudioPlayerInit( AudioPlayerInitEvent event ) {}
	@EventSubscriber public void onLoopStateChange( LoopStateChangeEvent event ) {}
	@EventSubscriber public void onPauseStateChange( PauseStateChangeEvent event ) {}
	@EventSubscriber public void onProcessorAdd( ProcessorAddEvent event ) {}
	@EventSubscriber public void onProcessorRemove( ProcessorRemoveEvent event ) {}
	@EventSubscriber public void onShuffle( ShuffleEvent event ) {}
	@EventSubscriber public void onTrackFinish( TrackFinishEvent event ) {}
	@EventSubscriber public void onTrackQueue( TrackQueueEvent event ) {}
	@EventSubscriber public void onTrackSkip( TrackSkipEvent event ) {}
	@EventSubscriber public void onTrackStart( TrackStartEvent event ) {}
	@EventSubscriber public void onVolumeChange( VolumeChangeEvent event ) {}

}
