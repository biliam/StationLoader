package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.TexturePackCustom;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TexturePackCustom.class)
public class MixinTexturePackCustom {

    @Redirect(method = "bindThumbnailTexture(Lnet/minecraft/client/Minecraft;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int texture, int textureID) {
        OpenGLHelper.bindTexture(texture, textureID);
    }
}
