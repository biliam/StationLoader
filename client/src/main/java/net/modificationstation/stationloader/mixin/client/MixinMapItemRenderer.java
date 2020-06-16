package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.MapItemRenderer;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MapItemRenderer.class)
public class MixinMapItemRenderer {

    @Redirect(method = "func_28157_a(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/MapData;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int target, int texture) {
        OpenGLHelper.bindTexture(target, texture);
    }
}
