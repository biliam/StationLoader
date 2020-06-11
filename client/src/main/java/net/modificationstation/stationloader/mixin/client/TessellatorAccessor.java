package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Invoker("<init>")
    static Tessellator createTessellator(int var1) {
        throw new IllegalStateException("Untransformed Accessor!");
    }

    @Accessor
    int getBufferSize();

    @Accessor
    ByteBuffer getByteBuffer();

    @Accessor
    IntBuffer getIntBuffer();

    @Accessor
    FloatBuffer getFloatBuffer();

    @Accessor
    boolean getUseVBO();

    @Accessor
    IntBuffer getVertexBuffers();
}
