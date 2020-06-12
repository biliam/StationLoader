package net.modificationstation.stationloader.client.textures;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TexturePackBase;
import net.modificationstation.stationloader.mixin.client.RenderEngineAccessor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    public static int registerTexture(TextureRegistries type, String pathToImage) {
        ModTextureStatic texture = new ModTextureStatic(type, pathToImage);
        textures = Arrays.copyOf(textures, textures.length + 1);
        textures[textures.length - 1] = texture;
        return texture.atlasID * type.texturesPerFile() + texture.iconIndex;
    }

    public static void registerAllTextures(RenderEngine renderEngine) throws IOException {
        for (ModTextureStatic texture : textures) {
            texture.setup();
            renderEngine.registerTextureFX(texture);
        }
    }

    @SuppressWarnings("unchecked")
    public static int getAtlasTexture(RenderEngine renderEngine, TextureRegistries type, int ID) {
        if (ID == 0)
            return renderEngine.getTexture(type.path());
        ID--;
        TexturePackBase texturePack = ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackList.selectedTexturePack;
        Map<String, Integer> textureMap = ((RenderEngineAccessor) renderEngine).getTextureMap();
        Integer var3 = textureMap.get(String.format("/assets/stationloader/atlases/station.%s.%d.png", type.name().toLowerCase(), ID));
        if (var3 != null)
            return var3;
        else {
            IntBuffer singleIntBuffer = ((RenderEngineAccessor) renderEngine).getSingleIntBuffer();
            BufferedImage missingTextureImage = ((RenderEngineAccessor) renderEngine).getMissingTextureImage();
            ((Buffer) singleIntBuffer).clear();
            GLAllocation.generateTextureNames(singleIntBuffer);
            int var6 = singleIntBuffer.get(0);
            InputStream var7 = texturePack.getResourceAsStream(String.format("/assets/stationloader/atlases/station.%s.png", type.name().toLowerCase()));
            if (var7 == null)
                renderEngine.setupTexture(missingTextureImage, var6);
            else
                renderEngine.setupTexture(((RenderEngineAccessor)renderEngine).invokeReadTextureImage(var7), var6);
            textureMap.put(String.format("/assets/stationloader/atlases/station.%s.%d.png", type.name().toLowerCase(), ID), var6);
            return var6;
        }
    }

    public static int nextSpriteID(TextureRegistries type) {
        if (!spriteIDs.containsKey(type))
            spriteIDs.put(type, type.texturesPerFile() - 1);
        int ID = spriteIDs.get(type) + 1;
        spriteIDs.put(type, ID);
        return ID;
    }

    private static ModTextureStatic[] textures = new ModTextureStatic[0];
    private static final Map<TextureRegistries, Integer> spriteIDs = new HashMap<>();
}
