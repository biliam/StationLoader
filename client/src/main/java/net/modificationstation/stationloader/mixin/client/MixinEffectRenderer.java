package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.*;
import net.modificationstation.stationloader.client.textures.TextureRegistries;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EffectRenderer.class)
public class MixinEffectRenderer {

    @Shadow private RenderEngine renderer;

    /*@Inject(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At("HEAD"))
    private void onRenderParticles(CallbackInfo ci) {
        currentTexture = -1;
    }

    @Inject(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 1, shift = At.Shift.AFTER))
    private void onTerrainTexture(CallbackInfo ci) {
        currentTexture = 0;
    }*/

    @Redirect(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I"))
    private int onGetTexture(RenderEngine renderEngine, String texture) {
        registryToBind = null;
        if (!TextureRegistries.currentBindRegistry().getAtlas(TextureRegistries.currentBindRegistry().currentTexture()).equals(texture))
            for (TextureRegistries registry : TextureRegistries.values()) {
                Integer atlasID = registry.getAtlasID(texture);
                if (atlasID != null) {
                    registryToBind = registry;
                    textureToBind = atlasID;
                    break;
                }
            }
        return renderEngine.getTexture(texture);
    }

    @Redirect(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int texture, int textureID) {
        if (registryToBind == null)
            GL11.glBindTexture(texture, textureID);
        else
            registryToBind.bindAtlas(renderer, textureToBind);
    }

    @Redirect(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityFX;renderParticle(Lnet/minecraft/src/Tessellator;FFFFFF)V"))
    private void onRenderParticles(EntityFX entityFX, Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
        if (TextureRegistries.currentBindRegistry() != null) {
            int atlasID = ((EntityFXAccessor) entityFX).getParticleTextureIndex() / TextureRegistries.currentBindRegistry().texturesPerFile();
            if (atlasID != TextureRegistries.currentBindRegistry().currentTexture()) {
                Tessellator tessellator = Tessellator.instance;
                tessellator.draw();
                TextureRegistries.currentBindRegistry().bindAtlas(renderer, atlasID);
                tessellator.startDrawingQuads();
            }
        }
        /*if (currentTexture != -1 && entityFX instanceof EntityDiggingFX) {
            EntityDiggingFX entityDiggingFX = (EntityDiggingFX) entityFX;
            int atlasID = ((EntityFXAccessor) entityDiggingFX).getParticleTextureIndex() / TERRAIN.texturesPerFile();
            if (TERRAIN.currentTexture() != atlasID) {
                Tessellator tessellator = Tessellator.instance;
                tessellator.draw();
                TERRAIN.bindAtlas(renderer, atlasID);
                tessellator.startDrawingQuads();
            }
        }*/
        entityFX.renderParticle(var1, var2, var3, var4, var5, var6, var7);
    }

    private TextureRegistries registryToBind;
    private int textureToBind;
    //private int currentTexture;
}
