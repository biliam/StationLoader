package net.modificationstation.stationloader.event.client.textures;

import net.modificationstation.stationloader.client.textures.TextureRegistries;
import net.modificationstation.stationloader.event.Event;
import net.modificationstation.stationloader.event.StationEvent;

/**
 * Event called when TexturesPerFile of a texture registry got changed, so mods can perform actions on change
 *
 * args: TextureRegistries
 * return: void
 *
 * @author mine_diver
 *
 */

public interface TexturesPerFileListener {

    Event<TexturesPerFileListener> EVENT = new StationEvent<>(TexturesPerFileListener.class, (listeners) ->
            (type) -> {
                for (TexturesPerFileListener event : listeners)
                    event.texturesPerFileChanged(type);
            });

    void texturesPerFileChanged(TextureRegistries type);
}

