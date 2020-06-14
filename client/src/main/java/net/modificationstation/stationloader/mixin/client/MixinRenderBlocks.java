package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.modificationstation.stationloader.client.textures.TextureManager;
import org.lwjgl.opengl.GL11;
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
    private int getTextureIDTorch(int texID) {
        return overrideTexture(texID);
    }

    private int overrideTexture(int texID) {
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
