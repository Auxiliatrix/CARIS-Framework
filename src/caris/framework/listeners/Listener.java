package caris.framework.listeners;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;

import caris.configuration.calibration.Constants;
import caris.framework.modules.Module;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.Reaction.Tag;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;

public abstract class Listener<E extends Event> {
	
	public abstract String getName();
	
	public Class<E> eventClass;
	
	protected Logger logger;
	protected List<Module<E>> modules;
	
	public Listener(Class<E> eventClass) {
		this.eventClass = eventClass;
		logger = new Logger().addOrigin(this.getName());
		
		this.modules = new ArrayList<Module<E>>();
		startup();
	}
	
	@SuppressWarnings("unchecked")
	public void startup() {
		Logger startupLogger = logger.clone().addOrigin("Startup");
		logger.log("Starting up " + this.getName());
		Reflections moduleClasses = new Reflections(Constants.SOURCE_PACKAGE);
		for( Class<?> c : moduleClasses.getSubTypesOf(caris.framework.modules.Module.class) ) {
			try {
				Module<?> module = (Module<?>) c.newInstance();
				if( eventClass == module.eventClass ) {
					modules.add((Module<E>) module);
					startupLogger.log("Module [" + module.getName() + "] loaded");
				}
			} catch (InstantiationException | IllegalAccessException e) {
				startupLogger.report("Instantiation failure.");
			}
		}
	}
	
	@EventSubscriber
	public void onReceive( E event ) {
		Map<Tag, List<Reaction>> reactionMap = new HashMap<Tag, List<Reaction>>();
		for( Tag tag : Reaction.Tag.values() ) {
			reactionMap.put(tag, new ArrayList<Reaction>());
		}
		for( Module<E> module : modules ) {
			Reaction reaction = module.handle(event);
			if( reaction != null ) {
				reactionMap.get(reaction.getTag()).add(reaction);
			}
		}
		
		List<Reaction> reactionQueue = new ArrayList<Reaction>();
		
		reactionQueue.addAll(reactionMap.get(Tag.PASSIVE));
		
		if( !reactionMap.get(Tag.DOMINANT).isEmpty() ) {
			reactionQueue.addAll(reactionMap.get(Tag.DOMINANT));
		} else if( !reactionMap.get(Tag.DEFAULT).isEmpty() ) {
			reactionQueue.addAll(reactionMap.get(Tag.DEFAULT));
		} else {
			reactionQueue.addAll(reactionMap.get(Tag.RECESSIVE));
		}
		
		for( Reaction reaction : reactionQueue ) {
			reaction.start();
		}
	}
	
}
