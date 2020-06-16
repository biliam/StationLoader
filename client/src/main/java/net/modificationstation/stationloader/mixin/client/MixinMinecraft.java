package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;
import net.modificationstation.stationloader.client.textures.OpenGLHelper;
import net.modificationstation.stationloader.client.textures.TextureManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationloader.client.textures.TextureRegistries.TERRAIN;

import java.io.IOException;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public RenderEngine renderEngine;

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;renderGlobal:Lnet/minecraft/src/RenderGlobal;", opcode = Opcodes.PUTFIELD))
    private void afterTextureFXRegister(CallbackInfo ci) throws IOException {
        TextureManager.setupAllTextures(renderEngine);
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindBlocksAtlas(int texture, int textureID) {
        TERRAIN.bindAtlas(renderEngine, 0);
    }

    @Redirect(method = "loadScreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture(int texture, int textureID) {
        OpenGLHelper.bindTexture(texture, textureID);
    }
}
