package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.modificationstation.stationloader.client.textures.TextureRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.TERRAIN;

@Mixin(RenderBlocks.class)
public class MixinRenderBlocks {

    @ModifyVariable(method = {
            "renderBottomFace(Lnet/minecraft/src/Block;DDDI)V",
            "renderTopFace(Lnet/minecraft/src/Block;DDDI)V",
            "renderEastFace(Lnet/minecraft/src/Block;DDDI)V",
            "renderWestFace(Lnet/minecraft/src/Block;DDDI)V",
            "renderNorthFace(Lnet/minecraft/src/Block;DDDI)V",
            "renderSouthFace(Lnet/minecraft/src/Block;DDDI)V"
    }, index = 8, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "renderTorchAtAngle(Lnet/minecraft/src/Block;DDDDD)V", index = 13, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getTorchTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "renderCrossedSquares(Lnet/minecraft/src/Block;IDDD)V", index = 10, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getCrossedTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "func_1245_b(Lnet/minecraft/src/Block;IDDD)V", index = 10, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getCropsTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "renderBlockFire(Lnet/minecraft/src/Block;III)Z", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;getBlockBrightness(Lnet/minecraft/src/IBlockAccess;III)F", shift = At.Shift.BEFORE))
    private int getFireTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "renderBlockRedstoneWire(Lnet/minecraft/src/Block;III)Z", index = 7, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;getBlockBrightness(Lnet/minecraft/src/IBlockAccess;III)F", shift = At.Shift.BEFORE))
    private int getRedstoneWireTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "renderBlockLadder(Lnet/minecraft/src/Block;III)Z", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;getBlockBrightness(Lnet/minecraft/src/IBlockAccess;III)F", shift = At.Shift.BEFORE))
    private int getLadderTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "renderBlockMinecartTrack(Lnet/minecraft/src/BlockRail;III)Z", index = 7, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockRail;getIsPowered()Z", shift = At.Shift.BEFORE))
    private int getRailTextureID(int texID) {
        return overrideTexture(texID);
    }

    private int overrideTexture(int texID) {
        if (TextureRegistries.currentBindRegistry() == TERRAIN) {
            int atlasID = texID / TERRAIN.texturesPerFile();
            if (TERRAIN.currentTexture() != atlasID) {
                Tessellator tessellator = Tessellator.instance;
                boolean hasColor = false;
                if (!inventory) {
                    hasColor = ((TessellatorAccessor) tessellator).getHasColor();
                    tessellator.draw();
                }
                TERRAIN.bindAtlas(((Minecraft) FabricLoader.getInstance().getGameInstance()).renderEngine, atlasID);
                if (!inventory) {
                    tessellator.startDrawingQuads();
                    ((TessellatorAccessor) tessellator).setHasColor(hasColor);
                }
            }
            return texID % TERRAIN.texturesPerFile();
        } else
            return texID;
    }

    @Inject(method = "renderBlockOnInventory(Lnet/minecraft/src/Block;IF)V", at = @At("HEAD"))
    private void onRenderBlockInInventory(CallbackInfo ci) {
        inventory = true;
        //currentTexture = 0;
    }

    @Inject(method = "renderBlockOnInventory(Lnet/minecraft/src/Block;IF)V", at = @At("RETURN"))
    private void afterRenderBlockInInventory(CallbackInfo ci) {
        inventory = false;
    }

    private boolean inventory;
}
