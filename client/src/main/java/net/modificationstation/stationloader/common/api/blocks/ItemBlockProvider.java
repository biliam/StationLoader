package net.modificationstation.stationloader.common.api.blocks;

import net.minecraft.src.ItemBlock;

public interface ItemBlockProvider {

    ItemBlock getItemBlock(int shiftedID);
}
