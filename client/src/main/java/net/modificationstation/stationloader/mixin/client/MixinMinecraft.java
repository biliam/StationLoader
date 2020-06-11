package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;
import net.modificationstation.stationloader.client.textures.TextureManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public RenderEngine renderEngine;

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;renderGlobal:Lnet/minecraft/src/RenderGlobal;", opcode = Opcodes.PUTFIELD))
    private void afterTextureFXRegister(CallbackInfo ci) throws IOException {
        TextureManager.registerAllTextures(renderEngine);
    }
}
