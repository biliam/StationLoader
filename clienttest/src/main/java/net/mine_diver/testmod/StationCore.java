package net.mine_diver.testmod;

import net.mine_diver.testmod.blocks.ModdedBlock;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.modificationstation.stationloader.client.textures.TextureManager;
import net.modificationstation.stationloader.client.textures.TextureRegistries;
import net.modificationstation.stationloader.common.api.StationMod;
import net.modificationstation.stationloader.common.recipes.craftingtable.CraftingTableManager;
import net.modificationstation.stationloader.event.common.blocks.BlockRegister;
import net.modificationstation.stationloader.event.common.recipes.RecipeRegister;

public class StationCore implements StationMod, BlockRegister, RecipeRegister {

    @Override
    public void preInit() {
        getLogger().info("A test output in logger on preInit");
        BlockRegister.EVENT.register(this);
        RecipeRegister.EVENT.register(this);
    }

    @Override
    public void registerBlocks() {
        testBlock = new ModdedBlock(150, TextureManager.registerTexture(TextureRegistries.TERRAIN, "/assets/testmod/textures/bruh_testBlock.png"), Material.cloth).setBlockName("testBlock");
        getLogger().info("Registered block!");
    }

    @Override
    public void registerRecipes(RecipeRegister.Type type) {
        switch(type) {
            case CRAFTING_TABLE: {
                getLogger().info("yay, crafting table recipes");
                CraftingTableManager.addRecipe(new ItemStack(testBlock, 64), "X", 'X', Block.dirt);
                CraftingTableManager.addRecipe(new ItemStack(Block.glowStone, 64), "X", 'X', testBlock);
                break;
            }
            default: {
                getLogger().info("wtf");
                break;
            }
        }
    }

    public Block testBlock;
}
