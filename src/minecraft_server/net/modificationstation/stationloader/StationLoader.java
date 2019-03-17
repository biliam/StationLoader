package net.modificationstation.stationloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.modificationstation.stationloader.events.Event;
import net.modificationstation.stationloader.util.Mod;

public class StationLoader {
	public static void startEvent(Event event) {
		for (Iterator<Class<?>> mods = loadedMods.iterator();mods.hasNext();){
			Class<?> mod = mods.next();
			try {getEventHandlerFor(mod, event).invoke(null, event);} catch (Exception e) {e.printStackTrace();}
		}
	}
	public static boolean addMod(Class<?> mod) {
		if(!loadedMods.contains(mod)){
			loadedMods.add(mod);
			return true;
		}
		return false;
	}
	public static Method getEventHandlerFor(Class<?> mod, Event event) {
		Method eventHandler = null;
		for (Method m : mod.getDeclaredMethods()){
			for (Annotation a : m.getAnnotations()) {
				if (a.annotationType().equals(Mod.EventHandler.class) && m.getParameterTypes().length == 1 && (m.getParameterTypes()[0].equals(event.getClass()))){
					eventHandler = m;
				}
			}
		}
		return eventHandler;
	}
	public static Logger LOGGER = Logger.getLogger("StationLoader");
	public static List<Class<?>> loadedMods = new ArrayList<Class<?>>();
}
