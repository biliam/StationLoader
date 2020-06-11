package net.modificationstation.stationloader.common.recipes.craftingtable;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.ItemStack;
import net.modificationstation.stationloader.mixin.common.CraftingManagerInvoker;

public class CraftingTableManager {

    private static CraftingManagerInvoker craftingManager;

    public static void setInstance(CraftingManager instance) {
        craftingManager = (CraftingManagerInvoker) instance;
    }

    public static void addRecipe(ItemStack itemStack, Object... o) {
        craftingManager.invokeAddRecipe(itemStack, o);
    }

    public static void addShapelessRecipe(ItemStack itemStack, Object... o) {
        craftingManager.invokeAddShapelessRecipe(itemStack, o);
    }
}
