package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.FontRenderer;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @Redirect(method = "renderString(Ljava/lang/String;IIIZ)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int target, int texture) {
        OpenGLHelper.bindTexture(target, texture);
    }
}
