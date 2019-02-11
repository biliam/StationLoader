package net.modificationstation.stationloader.events.common;

import java.util.Iterator;

import net.modificationstation.stationloader.common.StationLoader;
import net.modificationstation.stationloader.common.util.Mod;
import net.modificationstation.stationloader.common.util.ReflectionHelper;

public class Event {
	public final String getEventType(){
		return getClass().getSimpleName();
	}
    public void process() {
        for (Iterator<Class<?>> mods = StationLoader.loadedMods.iterator();mods.hasNext();){
            Class<?> mod = mods.next();
            try {
                ReflectionHelper.getMethodAnnotation(mod, Mod.EventHandler.class, getClass()).invoke(null, this);
            } catch (Exception e) {}
        }
    }
}
