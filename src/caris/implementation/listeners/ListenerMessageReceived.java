package caris.implementation.listeners;

import caris.framework.listeners.Listener;
import caris.implementation.events.MessageEventWrapper;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class ListenerMessageReceived extends Listener<MessageReceivedEvent> {
	
	private class Wrapper extends Listener<MessageEventWrapper> {
		
		@Override
		public String getName() {
			return "Wrapper";
		}
		
		public Wrapper() {
			super(MessageEventWrapper.class);
		}
		
	}
	
	private Wrapper wrapper;
	
	@Override
	public String getName() {
		return "MessageReceived";
	}
	
	public ListenerMessageReceived() {
		super(MessageReceivedEvent.class);
		wrapper = new Wrapper();
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
		wrapper.onReceive(new MessageEventWrapper(event));
	}
	
}
