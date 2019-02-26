package caris.modular.reactions;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.utilities.BotUtils;
import caris.modular.embedbuilders.TBABuilder;
import caris.modular.tokens.TBAMatchObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class TBAMatchAlertReaction extends Reaction {
	
	public IChannel channel;
	public TBAMatchObject[] queue;
	public TBAMatchObject match;
	public String team;
	public List<IUser> users;
	
	public TBAMatchAlertReaction(IChannel channel, TBAMatchObject[] queue, String team, List<IUser> users) {
		this.channel = channel;
		this.queue = queue;
		this.match = queue[0];
		this.team = team;
		this.users = users;
	}

	@Override
	public void process() {
		if( queue.length > 0 ) {
			List<String> mentions = new ArrayList<String>();
			for( IUser user : users ) {
				mentions.add(user.mention());
			}
			String messageContent = String.join(" ", mentions);
			BotUtils.sendMessage(channel, messageContent, TBABuilder.getAlertEmbed(queue[0], team));
		}
		if( queue.length > 1 ) {
			TBAMatchObject[] newQueue = new TBAMatchObject[queue.length-1];
			for( int f=1; f<queue.length; f++ ) {
				newQueue[f-1] = queue[f];
			}
			Brain.timedQueue.put(new TBAMatchAlertReaction(channel, newQueue, team, users), queue[1].predictedTime);
		}
	}

}
