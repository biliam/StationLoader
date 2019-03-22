package net.modificationstation.stationloader.events.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Events manager for easier and faster events control
 * 
 * @author ABLPHA
 *
 */
public class EventsManager {
    
    /**
     * Function that registers an event listener instance to events it implements
     * @param eventListener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void register(Object eventListener) {
        initializeEvents(eventListener.getClass().getInterfaces());
        for (InstancedEvent event : InstancedEvent.EVENTS)
            for (Class<?> interf : eventListener.getClass().getInterfaces())
                if (event.getType().equals(interf)) {
                    event.register(event.getType().cast(eventListener));
                    break;
                }
    }
    
    /**
     * Function that initializes events if they aren't presented in InstancedEvent.EVENTS yet
     * @param interfaces
     */
    private static void initializeEvents(Class<?> events[]) {
        for (Class<?> event : events)
            try {
                Class.forName(event.getName());
            } catch (ClassNotFoundException e) {e.printStackTrace();}
    }
}