package net.modificationstation.stationloader.client.textures;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ModTextureStatic extends TextureFX {

    public ModTextureStatic(TextureRegistries type, String pathToImage) {
        this(type, 1, pathToImage);
    }

    public ModTextureStatic(TextureRegistries type, int size, String pathToImage) {
        super(TextureManager.nextSpriteID(type));
        this.type = type;
        atlasID = iconIndex / this.type.texturesPerFile();
        iconIndex %= this.type.texturesPerFile();
        this.pathToImage = pathToImage;
        pixels = null;
        tileSize = size;
        tileImage = this.type.ordinal();
    }

    public void setup() throws IOException {
        Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
        bindImage(mc.renderEngine);
        int l = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, 4096 /*GL_TEXTURE_WIDTH*/) / 16;
        int i1 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D /*GL_TEXTURE_2D*/, 0, 4097 /*GL_TEXTURE_HEIGHT*/) / 16;
        BufferedImage bufferedimage = ImageIO.read(mc.texturePackList.selectedTexturePack.getResourceAsStream(pathToImage));
        int j1 = bufferedimage.getWidth();
        int k1 = bufferedimage.getHeight();
        pixels = new int[l * i1];
        imageData = new byte[l * i1 * 4];
        if(j1 != k1 || j1 != l) {
            BufferedImage bufferedimage1 = new BufferedImage(l, i1, 6);
            Graphics2D graphics2d = bufferedimage1.createGraphics();
            graphics2d.drawImage(bufferedimage, 0, 0, l, i1, 0, 0, j1, k1, null);
            bufferedimage1.getRGB(0, 0, l, i1, pixels, 0, l);
            graphics2d.dispose();
        } else
            bufferedimage.getRGB(0, 0, j1, k1, pixels, 0, j1);
        update();
    }

    public void update() {
        for(int i = 0; i < pixels.length; i++) {
            int j = pixels[i] >> 24 & 0xff;
            int k = pixels[i] >> 16 & 0xff;
            int l = pixels[i] >> 8 & 0xff;
            int i1 = pixels[i] & 0xff;
            if(anaglyphEnabled) {
                int j1 = (k + l + i1) / 3;
                k = l = i1 = j1;
            }
            imageData[i * 4] = (byte)k;
            imageData[i * 4 + 1] = (byte)l;
            imageData[i * 4 + 2] = (byte)i1;
            imageData[i * 4 + 3] = (byte)j;
        }

        oldanaglyph = anaglyphEnabled;
    }

    @Override
    public void onTick() {
        if(oldanaglyph != anaglyphEnabled)
            update();
    }

    @Override
    public void bindImage(RenderEngine renderEngine) {
        type.bindAtlas(renderEngine, atlasID);
    }

    private final TextureRegistries type;
    public final int atlasID;
    private final String pathToImage;
    private boolean oldanaglyph;
    private int[] pixels;
}
