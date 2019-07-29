package caris.implementation.listeners;

import caris.framework.listeners.Listener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class ListenerMessageReceived extends Listener<MessageReceivedEvent> {
	
	@Override
	public String getName() {
		return "MessageReceived";
	}
	
	public ListenerMessageReceived() {
		super(MessageReceivedEvent.class);
	}
	
	@Override
	@EventSubscriber
	public void onReceive( MessageReceivedEvent event ) {
		logger.clone()
			.setType("LISTEN")
			.setTimeStamp(event.getMessage().getTimestamp())
			.addOrigin(event.getGuild().getName() + ":" + event.getChannel().getName())
			.addOrigin(event.getAuthor().getName())
			.log(event.getMessage().getContent());
		
		super.onReceive(event);
	}
	
}
