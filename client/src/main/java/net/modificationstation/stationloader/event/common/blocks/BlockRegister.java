package net.modificationstation.stationloader.event.common.blocks;

import net.modificationstation.stationloader.event.Event;
import net.modificationstation.stationloader.event.StationEvent;

/**
 * Event called after vanilla blocks registering
 * 
 * args: none
 * return: void
 * 
 * @author mine_diver
 *
 */

public interface BlockRegister {

	Event<BlockRegister> EVENT = new StationEvent<>(BlockRegister.class, (listeners) ->
			() -> {
		for (BlockRegister event : listeners)
			event.registerBlocks();
	});

	void registerBlocks();
}
