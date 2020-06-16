package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityRenderer;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.*;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Shadow private Minecraft mc;

    @Redirect(method = "renderRainSnow(F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int texture, int textureID) {
        OpenGLHelper.bindTexture(texture, textureID);
    }

    @Redirect(method = "renderWorld(FJ)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTerrain(int texture, int textureID) {
        TERRAIN.bindAtlas(mc.renderEngine, 0);
    }
}
