package caris.framework.main;

import java.awt.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;

import caris.framework.modules.Module;

public class Brain {

	@SuppressWarnings("rawtypes")
	public static List<Module> modules = new ArrayList<Module>();
	
	public static void main(String[] args) {
		loadModules("caris.framework.modules", "");
		loadModules("caris.extended.modules", "");
	}

	private static void loadModules(String includePrefix, String excludePrefix) {
		System.out.print("Loading Handlers from \"" + includePrefix + "\":");
		Reflections include = new Reflections(includePrefix);
		Reflections exclude = excludePrefix.isEmpty() ? null : new Reflections(excludePrefix);
		for( Class<?> c : include.getSubTypesOf(caris.framework.modules.Module.class) ) {
			if( exclude != null && exclude.getSubTypesOf(caris.framework.modules.Module.class).contains(c) ) {
				continue;
			}
			try {
				@SuppressWarnings("rawtypes")
				Module m = (Module) c.newInstance();
				modules.add(m);
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println("Failed to load module.");
			}
		}
	}

	
}
