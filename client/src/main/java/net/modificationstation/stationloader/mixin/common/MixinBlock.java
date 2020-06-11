package net.modificationstation.stationloader.mixin.common;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.modificationstation.stationloader.common.blocks.BlockManager;
import net.modificationstation.stationloader.event.common.blocks.BlockRegister;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {

    @Shadow
    public static @Final Block[] blocksList;

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Block;trapdoor:Lnet/minecraft/src/Block;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private static void onBlockRegister(CallbackInfo ci) {
        BlockRegister.EVENT.getInvoker().registerBlocks();
    }

    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(I)Lnet/minecraft/src/ItemBlock;"))
    private static ItemBlock createItemBlock(int itemID) {
        return BlockManager.getItemBlock(blocksList[itemID + blocksList.length]);
    }
}

