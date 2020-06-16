package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.TERRAIN;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Shadow private RenderEngine renderEngine;

    @Redirect(method = "drawBlockBreaking(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/MovingObjectPosition;ILnet/minecraft/src/ItemStack;F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTerrain(int texture, int textureID) {
        TERRAIN.bindAtlas(renderEngine, 0);
    }

    @Redirect(method = {"renderClouds(F)V", "renderCloudsFancy(F)V", "renderSky(F)V"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int texture, int textureID) {
        OpenGLHelper.bindTexture(texture, textureID);
    }
}
