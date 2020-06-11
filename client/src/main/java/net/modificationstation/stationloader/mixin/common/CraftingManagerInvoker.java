package net.modificationstation.stationloader.mixin.common;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CraftingManager.class)
public interface CraftingManagerInvoker {

    @Invoker
    void invokeAddRecipe(ItemStack var1, Object... var2);
    @Invoker
    void invokeAddShapelessRecipe(ItemStack var1, Object... var2);
}
