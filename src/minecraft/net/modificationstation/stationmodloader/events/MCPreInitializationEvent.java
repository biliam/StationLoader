package net.modificationstation.stationmodloader.events;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.modificationstation.classloader.ClassLoadingManager;
import net.modificationstation.classloader.Log;
import net.modificationstation.classloader.LogFormatter;
import net.modificationstation.classloader.ReflectionHelper;
import net.modificationstation.stationmodloader.StationModLoader;
import net.modificationstation.stationmodloader.util.Mod;

public class MCPreInitializationEvent extends ModEvent{
    public MCPreInitializationEvent() {
        specificModEvent = false;
    }
    public MCPreInitializationEvent(Object instance) {
        specificModEvent = true;
        eventData = new Object[] {instance, instance.getClass().getAnnotation(Mod.class).name() == "" ? instance.getClass().getAnnotation(Mod.class).modid() : instance.getClass().getAnnotation(Mod.class).name()};
    }
    public Logger getModLog() {
        Logger logger = Logger.getLogger((String) eventData[1]);
        logger.setParent(Log.log.getLogger());
        return logger;
    }
    @Override
    public ModEvent process() {
        if (specificModEvent) {
            Method[] methods = ReflectionHelper.getMethodsAnnotation(eventData[0].getClass(), Mod.EventHandler.class, getClass());
            for (Method m : methods)
                try {
                	m.setAccessible(true);
                    m.invoke(eventData[0], this);
                } catch (NullPointerException e) {continue;} catch (Exception e) {e.printStackTrace();}
            }
        else
            for (Iterator<Object> mods = StationModLoader.loadedMods.iterator();mods.hasNext();) {
                Object instance = mods.next();
                ModEvent modSpecificEvent = new MCPreInitializationEvent(instance);
                modSpecificEvent.process();
            }
        return this;
    }
    private Object eventData[];
    private final boolean specificModEvent;
}
