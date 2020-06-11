package net.modificationstation.stationloader.event.common;

import net.modificationstation.stationloader.event.Event;
import net.modificationstation.stationloader.event.StationEvent;

/**
 * Event called before Minecraft launch
 *
 * args: none
 * return: void
 *
 * @author mine_diver
 *
 */

public interface PreInit {

    Event<PreInit> EVENT = new StationEvent<>(PreInit.class, (listeners) ->
            () -> {
        for (PreInit event : listeners)
            event.preInit();
    });

    void preInit();
}
