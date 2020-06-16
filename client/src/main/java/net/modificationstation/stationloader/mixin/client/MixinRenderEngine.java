package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.RenderEngine;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import net.modificationstation.stationloader.client.textures.TextureRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderEngine.class)
public class MixinRenderEngine {

    @Redirect(method = "bindTexture(I)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture1(int target, int texture) {
        for (TextureRegistries registry : TextureRegistries.values())
            if (registry.getAtlasTexture((RenderEngine) (Object) this, 0) == texture) {
                registry.bindAtlas((RenderEngine) (Object) this, 0);
                return;
            }
        OpenGLHelper.bindTexture(target, texture);
    }

    @Redirect(method = {"setupTexture(Ljava/awt/image/BufferedImage;I)V", "func_28150_a([IIII)V", "updateDynamicTextures"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture2(int target, int texture) {
        OpenGLHelper.bindTexture(target, texture);
    }
}
