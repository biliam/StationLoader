package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemRenderer;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.TERRAIN;
import static net.modificationstation.stationloader.client.textures.TextureRegistries.GUI_ITEMS;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Shadow private Minecraft mc;

    @Redirect(method = "renderItem(Lnet/minecraft/src/EntityLiving;Lnet/minecraft/src/ItemStack;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0))
    private void onBindTerrain1(int target, int texture) {
        TERRAIN.bindAtlas(mc.renderEngine, 0);
    }

    @Redirect(method = "renderItem(Lnet/minecraft/src/EntityLiving;Lnet/minecraft/src/ItemStack;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1))
    private void onBindTerrain2(int target, int texture) {
        TERRAIN.bindAtlas(mc.renderEngine, 0);
    }

    @Redirect(method = "renderItem(Lnet/minecraft/src/EntityLiving;Lnet/minecraft/src/ItemStack;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 2))
    private void onBindGuiItems(int target, int texture) {
        GUI_ITEMS.bindAtlas(mc.renderEngine, 0);
    }

    @Redirect(method = "renderItemInFirstPerson(F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture1(int target, int texture) {
        OpenGLHelper.bindTexture(target, texture);
    }

    @Redirect(method = "renderOverlays(F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0))
    private void onBindTerrain3(int target, int texture) {
        TERRAIN.bindAtlas(mc.renderEngine, 0);
    }

    @Redirect(method = "renderOverlays(F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1))
    private void onBindTerrain4(int target, int texture) {
        TERRAIN.bindAtlas(mc.renderEngine, 0);
    }

    @Redirect(method = "renderOverlays(F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 2))
    private void onBindTexture2(int target, int texture) {
        OpenGLHelper.bindTexture(target, texture);
    }
}
