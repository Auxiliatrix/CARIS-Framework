package caris.modular.reactions;

import org.json.JSONArray;

import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.modular.handlers.TBAHandler;
import caris.modular.tokens.TBAMatchObject;
import caris.modular.utilities.APIRetriever;
import caris.modular.utilities.TBAObjectFactory;

public class TBAMatchTimeUpdateReaction extends Reaction {

	public String event;
	public String team;
	
	public TBAMatchTimeUpdateReaction(String event, String team) {
		this.event = event;
		this.team = team;
	}
	
	@Override
	public void process() {
		boolean empty = true;
		for( Reaction reaction : Brain.timedQueue.keySet() ) {
			if( reaction instanceof TBAMatchAlertReaction ) {
				TBAMatchAlertReaction newMatchAlert = (TBAMatchAlertReaction) reaction;
				JSONArray queueArray = APIRetriever.getJSONArray(TBAHandler.TBA_ENDPOINT + "team/frc" + team + "/event/" + event + "/matches");
				if( queueArray != null ) {
					TBAMatchObject[] queue = TBAObjectFactory.generateTBAMatchQueue(queueArray);
					if( queue != null ) {
						for( TBAMatchObject match : queue ) {
							if( newMatchAlert.match.eventKey.equalsIgnoreCase(match.eventKey) && newMatchAlert.match.matchType.equals(match.matchType) && newMatchAlert.match.matchNumber == match.matchNumber ) {
								empty = false;
								Long time = Brain.timedQueue.get(reaction);
								if( Math.abs(time - match.predictedTime) > 1000 ) {
									Brain.timedQueue.remove(reaction);
									Brain.timedQueue.put(reaction, match.predictedTime);
								}
							}
						}
					}
				}
			}
		}
		if( !empty ) {
			Brain.timedQueue.put(new TBAMatchTimeUpdateReaction(event, team), System.currentTimeMillis()+1000);
		}
	}
	
}
