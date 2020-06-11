package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.WorldRenderer;
import net.modificationstation.stationloader.client.textures.ModTextureStatic;
import net.modificationstation.stationloader.client.textures.TextureManager;
import net.modificationstation.stationloader.client.textures.TextureRegistries;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    /*@Shadow private static Tessellator tessellator;

    @Redirect(method = "updateRenderer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderBlockByRenderType(Lnet/minecraft/src/Block;III)Z"))
    private boolean onBlockRender(RenderBlocks renderBlocks, Block block, int i, int i1, int i2) {
        boolean reset = false;
        if (block.blockID == 150) {
            GL11.glBindTexture(3553, TextureManager.getAtlasTexture(((Minecraft) FabricLoader.getInstance().getGameInstance()).renderEngine, TextureRegistries.TERRAIN, block.blockIndexInTexture / 256));
            reset = true;
            System.out.println("huuuh");
        }
        boolean ret = renderBlocks.renderBlockByRenderType(block, i, i1, i2);
        if (reset)
            GL11.glBindTexture(3553, ((Minecraft) FabricLoader.getInstance().getGameInstance()).renderEngine.getTexture(TextureRegistries.TERRAIN.path()));
        return ret;
    }*/
}
