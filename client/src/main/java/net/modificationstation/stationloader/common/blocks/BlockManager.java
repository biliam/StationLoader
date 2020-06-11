package net.modificationstation.stationloader.common.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.modificationstation.stationloader.common.api.blocks.ItemBlockProvider;

public class BlockManager {

    public static ItemBlock getItemBlock(Block block) {
        int shiftedID = block.blockID - Block.blocksList.length;
        if (block instanceof ItemBlockProvider)
            return ((ItemBlockProvider) block).getItemBlock(shiftedID);
        else
            return new ItemBlock(shiftedID);
    }
}
