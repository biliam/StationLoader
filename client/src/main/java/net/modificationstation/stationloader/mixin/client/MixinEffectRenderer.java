package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.*;
import net.modificationstation.stationloader.client.textures.TextureManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.TERRAIN;

@Mixin(EffectRenderer.class)
public class MixinEffectRenderer {

    @Shadow private RenderEngine renderer;

    @Inject(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At("HEAD"))
    private void onRenderParticles(CallbackInfo ci) {
        currentTexture = -1;
    }

    @Inject(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 1, shift = At.Shift.AFTER))
    private void onTerrainTexture(CallbackInfo ci) {
        currentTexture = 0;
    }

    @Redirect(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityFX;renderParticle(Lnet/minecraft/src/Tessellator;FFFFFF)V"))
    private void onRenderParticles(EntityFX entityFX, Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
        if (currentTexture != -1 && entityFX instanceof EntityDiggingFX) {
            EntityDiggingFX entityDiggingFX = (EntityDiggingFX) entityFX;
            int atlasID = ((EntityFXAccessor) entityDiggingFX).getParticleTextureIndex() / TERRAIN.texturesPerFile();
            if (currentTexture != atlasID) {
                Tessellator tessellator = Tessellator.instance;
                tessellator.draw();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureManager.getAtlasTexture(renderer, TERRAIN, atlasID));
                currentTexture = atlasID;
                tessellator.startDrawingQuads();
            }
        }
        entityFX.renderParticle(var1, var2, var3, var4, var5, var6, var7);
    }

    private int currentTexture;
}
