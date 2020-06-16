package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.*;

@Mixin(TextureFX.class)
public class MixinTextureFX {

    @Redirect(method = "bindImage(Lnet/minecraft/src/RenderEngine;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0))
    private void onBindTerrain(int target, int texture, RenderEngine renderEngine) {
        TERRAIN.bindAtlas(renderEngine, 0);
    }

    @Redirect(method = "bindImage(Lnet/minecraft/src/RenderEngine;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1))
    private void onBindGuiItems(int target, int texture, RenderEngine renderEngine) {
        GUI_ITEMS.bindAtlas(renderEngine, 0);
    }
}
