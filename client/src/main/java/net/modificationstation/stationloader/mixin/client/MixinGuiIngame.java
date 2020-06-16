package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiIngame;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.TERRAIN;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Shadow private Minecraft mc;

    @Redirect(method = {"renderGameOverlay(FZII)V", "renderPumpkinBlur(II)V", "renderVignette(FII)V"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int target, int texture) {
        OpenGLHelper.bindTexture(target, texture);
    }

    @Redirect(method = "renderPortalOverlay(FII)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTerrain(int target, int texture) {
        TERRAIN.bindAtlas(mc.renderEngine, 0);
    }
}
