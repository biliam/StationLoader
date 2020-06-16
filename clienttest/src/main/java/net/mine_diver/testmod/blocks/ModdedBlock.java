package net.mine_diver.testmod.blocks;

import net.mine_diver.testmod.StationCore;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;
import net.modificationstation.stationloader.common.api.StationLoader;
import net.modificationstation.stationloader.common.api.blocks.ItemBlockProvider;

import java.util.Random;

public class ModdedBlock extends Block implements ItemBlockProvider {

    public ModdedBlock(int i, int i1, Material material) {
        super(i, i1, material);
    }

    @Override
    public ItemBlock getItemBlock(int shiftedID) {
        StationLoader.getInstance().getModInstance(StationCore.class).getLogger().info("ItemBlock success!");
        return new ItemBlock(shiftedID);
    }

    /*@Override
    public int getRenderType() {
        return 11;
    }*/

    /*@Override
    public int getBlockTextureFromSide(int side) {
        return rand.nextBoolean() ? blockIndexInTexture : 1;
    }*/

    private Random rand = new Random();
}
