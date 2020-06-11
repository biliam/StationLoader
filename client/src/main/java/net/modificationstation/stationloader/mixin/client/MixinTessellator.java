package net.modificationstation.stationloader.mixin.client;

import net.minecraft.src.GLAllocation;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

@Mixin(Tessellator.class)
public class MixinTessellator {

    @Shadow private int bufferSize;

    @Shadow @Final public static Tessellator instance;

    @Shadow private int[] rawBuffer;

    @Shadow private int rawBufferIndex;

    @Shadow private boolean useVBO;

    @Redirect(method = "<init>(I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Tessellator;bufferSize:I", opcode = Opcodes.PUTFIELD))
    private void getBufferSize(Tessellator tessellator, int bufferSize) {
        this.bufferSize = instance == null ? bufferSize : ((TessellatorAccessor) instance).getBufferSize();
    }

    @Redirect(method = "<init>(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GLAllocation;createDirectByteBuffer(I)Ljava/nio/ByteBuffer;"))
    private ByteBuffer allocateByteBuffer(int i) {
        return instance == null ? GLAllocation.createDirectByteBuffer(i) : ((TessellatorAccessor) instance).getByteBuffer();
    }

    @Redirect(method = "<init>(I)V", at = @At(value = "INVOKE", target = "Ljava/nio/ByteBuffer;asIntBuffer()Ljava/nio/IntBuffer;"))
    private IntBuffer asIntBuffer(ByteBuffer byteBuffer) {
        return instance == null ? byteBuffer.asIntBuffer() : ((TessellatorAccessor) instance).getIntBuffer();
    }

    @Redirect(method = "<init>(I)V", at = @At(value = "INVOKE", target = "Ljava/nio/ByteBuffer;asFloatBuffer()Ljava/nio/FloatBuffer;"))
    private FloatBuffer asFloatBuffer(ByteBuffer byteBuffer) {
        return instance == null ? byteBuffer.asFloatBuffer() : ((TessellatorAccessor) instance).getFloatBuffer();
    }

    @Inject(method = "<init>(I)V", at = @At("RETURN"))
    private void onReturn(CallbackInfo ci) {
        this.rawBuffer = null;
        rawBufferSize = 0;
    }

    /*@Redirect(method = "<init>(I)V", at = @At(value = "NEW", target = "Lnet/minecraft/src/Tessellator;<init>(I)V", args = "log=true"))
    private void getRawBuffer(Tessellator tessellator, int[] rawBuffer) {

    }*/

    @Inject(method = "addVertex(DDD)V", at = @At("HEAD"))
    private void onAddVertex(CallbackInfo ci) {
        if(rawBufferIndex >= rawBufferSize - 32) {
            if(rawBufferSize == 0) {
                rawBufferSize = 0x10000;
                rawBuffer = new int[rawBufferSize];
            } else {
                rawBufferSize *= 2;
                rawBuffer = Arrays.copyOf(rawBuffer, rawBufferSize);
            }
        }
    }

    @Redirect(method = "<init>(I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Tessellator;useVBO:Z", opcode = Opcodes.PUTFIELD))
    private void getUseVBO(Tessellator tessellator, boolean flag) {
        useVBO = instance == null ? flag : ((TessellatorAccessor) tessellator).getUseVBO();
    }

    @Redirect(method = "<init>(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GLAllocation;createDirectIntBuffer(I)Ljava/nio/IntBuffer;"))
    private IntBuffer allocateIntBuffer(int i) {
        return instance == null ? GLAllocation.createDirectIntBuffer(i) : ((TessellatorAccessor) instance).getVertexBuffers();
    }

    @Redirect(method = "<init>(I)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/ARBVertexBufferObject;glGenBuffersARB(Ljava/nio/IntBuffer;)V"))
    private void genBuffersARB(IntBuffer vertexBuffers) {
        if (instance == null)
            ARBVertexBufferObject.glGenBuffersARB(vertexBuffers);
    }

    private int rawBufferSize;
}
