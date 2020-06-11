package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityFX.class)
public interface EntityFXAccessor {

    @Accessor
    int getParticleTextureIndex();
}
