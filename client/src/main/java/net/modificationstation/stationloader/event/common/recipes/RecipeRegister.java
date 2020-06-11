package net.modificationstation.stationloader.event.common.recipes;

import net.modificationstation.stationloader.event.Event;
import net.modificationstation.stationloader.event.StationEvent;

/**
 * Event called after one of vanilla recipes system got initialized (CRAFTING_TABLE, FURNACE, etc)
 *
 * args: RecipeRegister.Type
 * return: void
 *
 * @author mine_diver
 *
 */

public interface RecipeRegister {

    enum Type {

        CRAFTING_TABLE
    }

    Event<RecipeRegister> EVENT = new StationEvent<>(RecipeRegister.class, (listeners) ->
            (type) -> {
        for (RecipeRegister event : listeners)
            event.registerRecipes(type);
    });

    void registerRecipes(Type recipeType);
}
