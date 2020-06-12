package net.modificationstation.stationloader.mixin.common;

import net.minecraft.src.CraftingManager;
import net.modificationstation.stationloader.common.recipes.craftingtable.CraftingTableManager;
import net.modificationstation.stationloader.event.common.recipes.RecipeRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Comparator;
import java.util.List;

@Mixin(CraftingManager.class)
public class MixinCraftingManager {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"))
    private <T> void onRecipesRegister(List<T> list, Comparator<? super T> c) {
        CraftingTableManager.setInstance((CraftingManager) (Object) this);
        RecipeRegister.EVENT.getInvoker().registerRecipes(RecipeRegister.Type.CRAFTING_TABLE);
        list.sort(c);
    }
}
