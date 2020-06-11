package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.MinecraftApplet;
import net.modificationstation.stationloader.common.api.StationLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftApplet.class)
public class MixinMinecraftApplet {

    @Inject(method = "init", at = @At("HEAD"))
    private void beforeInit(CallbackInfo ci) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        StationLoader.getInstance().setup();
    }
}
