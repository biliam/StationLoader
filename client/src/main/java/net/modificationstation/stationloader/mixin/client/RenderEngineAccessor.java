package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.RenderEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;

@Mixin(RenderEngine.class)
public interface RenderEngineAccessor {

    @Accessor
    @SuppressWarnings("rawtypes")
    HashMap getTextureMap();
    @Accessor
    IntBuffer getSingleIntBuffer();
    @Accessor
    BufferedImage getMissingTextureImage();
    @Invoker
    BufferedImage invokeReadTextureImage(InputStream var1);
}
