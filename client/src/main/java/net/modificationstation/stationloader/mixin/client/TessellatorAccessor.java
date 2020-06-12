package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Accessor
    boolean getHasColor();

    @Accessor
    void setHasColor(boolean hasColor);
}
