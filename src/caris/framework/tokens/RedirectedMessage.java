package caris.framework.tokens;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.vdurmont.emoji.Emoji;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IShard;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageTokenizer;

public class RedirectedMessage implements IMessage {

	protected IMessage message;
	protected IChannel redirect;
	protected String content;
	
	public RedirectedMessage(IMessage message, IChannel redirect, String content) {
		this.message = message;
		this.redirect = redirect;
		this.content = content;
	}
	
	@Override
	public IDiscordClient getClient() {
		return message.getClient();
	}

	@Override
	public IShard getShard() {
		return message.getShard();
	}

	@Override
	public IMessage copy() {
		return message.copy();
	}

	@Override
	public long getLongID() {
		return message.getLongID();
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public IChannel getChannel() {
		return redirect;
	}

	@Override
	public IUser getAuthor() {
		return message.getAuthor();
	}

	@Override
	public LocalDateTime getTimestamp() {
		return message.getTimestamp();
	}

	@Override
	public List<IUser> getMentions() {
		return message.getMentions();
	}

	@Override
	public List<IRole> getRoleMentions() {
		return message.getRoleMentions();
	}

	@Override
	public List<IChannel> getChannelMentions() {
		return message.getChannelMentions();
	}

	@Override
	public List<Attachment> getAttachments() {
		return message.getAttachments();
	}

	@Override
	public List<IEmbed> getEmbeds() {
		return message.getEmbeds();
	}

	@Override
	public IMessage reply(String content) {
		return message.reply(content);
	}

	@Override
	public IMessage reply(String content, EmbedObject embed) {
		return message.reply(content, embed);
	}

	@Override
	public IMessage edit(String content) {
		return message.edit(content);
	}

	@Override
	public IMessage edit(String content, EmbedObject embed) {
		return message.edit(content, embed);
	}

	@Override
	public IMessage edit(EmbedObject embed) {
		return message.edit(embed);
	}

	@Override
	public boolean mentionsEveryone() {
		return message.mentionsEveryone();
	}

	@Override
	public boolean mentionsHere() {
		return message.mentionsHere();
	}

	@Override
	public void delete() {
		message.delete();
	}

	@Override
	public Optional<LocalDateTime> getEditedTimestamp() {
		return message.getEditedTimestamp();
	}

	@Override
	public boolean isPinned() {
		return message.isPinned();
	}

	@Override
	public IGuild getGuild() {
		return message.getGuild();
	}

	@Override
	public String getFormattedContent() {
		return message.getFormattedContent();
	}

	@Override
	public List<IReaction> getReactions() {
		return message.getReactions();
	}

	@Override
	public IReaction getReactionByIEmoji(IEmoji emoji) {
		return message.getReactionByEmoji(emoji);
	}

	@Override
	public IReaction getReactionByEmoji(IEmoji emoji) {
		return message.getReactionByEmoji(emoji);
	}

	@Override
	public IReaction getReactionByID(long id) {
		return message.getReactionByID(id);
	}

	@Override
	public IReaction getReactionByUnicode(Emoji unicode) {
		return message.getReactionByUnicode(unicode);
	}

	@Override
	public IReaction getReactionByUnicode(String unicode) {
		return message.getReactionByUnicode(unicode);
	}

	@Override
	public IReaction getReactionByEmoji(ReactionEmoji emoji) {
		return message.getReactionByEmoji(emoji);
	}

	@Override
	public void addReaction(IReaction reaction) {
		message.addReaction(reaction);
	}

	@Override
	public void addReaction(IEmoji emoji) {
		message.addReaction(emoji);
	}

	@Override
	public void addReaction(Emoji emoji) {
		message.addReaction(emoji);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addReaction(String emoji) {
		message.addReaction(emoji);
	}

	@Override
	public void addReaction(ReactionEmoji emoji) {
		message.addReaction(emoji);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void removeReaction(IReaction reaction) {
		message.removeReaction(reaction);
	}

	@Override
	public void removeReaction(IUser user, IReaction reaction) {
		message.removeReaction(user, reaction);
	}

	@Override
	public void removeReaction(IUser user, ReactionEmoji emoji) {
		message.removeReaction(user, emoji);
	}

	@Override
	public void removeReaction(IUser user, IEmoji emoji) {
		message.removeReaction(user, emoji);
	}

	@Override
	public void removeReaction(IUser user, Emoji emoji) {
		message.removeReaction(user, emoji);
	}

	@Override
	public void removeReaction(IUser user, String emoji) {
		 message.removeReaction(user, emoji);
	}

	@Override
	public void removeAllReactions() {
		message.removeAllReactions();
	}

	@Override
	public MessageTokenizer tokenize() {
		return message.tokenize();
	}

	@Override
	public boolean isDeleted() {
		return message.isDeleted();
	}

	@Override
	public long getWebhookLongID() {
		return message.getWebhookLongID();
	}

	@Override
	public Type getType() {
		return message.getType();
	}

	@Override
	public boolean isSystemMessage() {
		return message.isSystemMessage();
	}

}
