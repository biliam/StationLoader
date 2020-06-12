package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

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
