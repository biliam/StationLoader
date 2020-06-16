package net.modificationstation.stationloader.client.textures;

import org.lwjgl.opengl.GL11;

public class OpenGLHelper {

    public static void bindTexture(int target, int texture) {
        TextureRegistries.unbind();
        GL11.glBindTexture(target, texture);
    }
}
