package net.modificationstation.stationloader.events.common;

import java.util.Iterator;

import net.modificationstation.stationloader.common.StationLoader;
import net.modificationstation.stationloader.common.util.ReflectionHelper;
import net.modificationstation.stationloader.common.util.SubscribeEvent;

public class MCEvent extends Event{
    @Override
    public void process() {
        for (Iterator<Class<?>> subscriptors = StationLoader.eventBusSubscriptors.iterator();subscriptors.hasNext();){
            Class<?> subscriptor = subscriptors.next();
            try {
                ReflectionHelper.getMethodAnnotation(subscriptor, SubscribeEvent.class, getClass()).invoke(null, this);
            } catch (NullPointerException e) {continue;} catch (Exception e) {e.printStackTrace();}
        }
    }
}
