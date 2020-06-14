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
import java.util.TreeMap;

public class TextureManager {

    public static int addTexture(TextureRegistries type, String pathToImage) {
        ModTextureStatic texture = new ModTextureStatic(type, pathToImage);
        if (setup)
            try {
                setupTexture(((Minecraft) FabricLoader.getInstance().getGameInstance()).renderEngine, texture);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        textures = Arrays.copyOf(textures, textures.length + 1);
        textures[textures.length - 1] = texture;
        return texture.atlasID * type.texturesPerFile() + texture.iconIndex;
    }

    public static void setupAllTextures(RenderEngine renderEngine) throws IOException {
        if (!setup) {
            setup = true;
            for (TextureRegistries type : spriteIDs.keySet())
                for (int atlas : spriteIDs.get(type).keySet())
                    createAtlasCopy(type, stationOriginalAtlas, atlas, String.format(stationCopiedAtlas, "%s", "%d"));
            for (ModTextureStatic texture : textures)
                setupTexture(renderEngine, texture);
        }
    }

    private static void setupTexture(RenderEngine renderEngine, ModTextureStatic texture) throws IOException {
        texture.setup();
        renderEngine.registerTextureFX(texture);
    }

    public static int createNewAtlas(TextureRegistries type, String originalAtlas, String path) {
        int ret = type.addAtlas(String.format(path, type.name().toLowerCase(), "%d"));
        if (setup)
            createAtlasCopy(type, originalAtlas, ret, path);
        return ret;
    }

    public static int createAtlasCopy(TextureRegistries type, String originalAtlas, int ID, String path) {
        originalAtlas = String.format(originalAtlas, type.name().toLowerCase());
        path = String.format(path, type.name().toLowerCase(), ID);
        Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
        TexturePackBase texturePack = mc.texturePackList.selectedTexturePack;
        RenderEngine renderEngine = mc.renderEngine;
        Map<String, Integer> textureMap = ((RenderEngineAccessor) renderEngine).getTextureMap();
        IntBuffer singleIntBuffer = ((RenderEngineAccessor) renderEngine).getSingleIntBuffer();
        BufferedImage missingTextureImage = ((RenderEngineAccessor) renderEngine).getMissingTextureImage();
        ((Buffer) singleIntBuffer).clear();
        GLAllocation.generateTextureNames(singleIntBuffer);
        int var6 = singleIntBuffer.get(0);
        InputStream var7 = texturePack.getResourceAsStream(originalAtlas);
        if (var7 == null)
            renderEngine.setupTexture(missingTextureImage, var6);
        else
            renderEngine.setupTexture(((RenderEngineAccessor) renderEngine).invokeReadTextureImage(var7), var6);
        textureMap.put(path, var6);
        return var6;
    }

    public static int nextSpriteID(TextureRegistries type) {
        if (!spriteIDs.containsKey(type)) {
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            int atlasID = createNewAtlas(type, stationOriginalAtlas, stationCopiedAtlas);
            treeMap.put(atlasID, 0);
            spriteIDs.put(type, treeMap);
        }
        TreeMap<Integer, Integer> atlases = spriteIDs.get(type);
        if (atlases.get(atlases.lastKey()) >= type.texturesPerFile())
            atlases.put(createNewAtlas(type, stationOriginalAtlas, stationCopiedAtlas), 0);
        int atlasID = atlases.lastKey();
        int spriteID = atlasID * type.texturesPerFile() + atlases.get(atlasID);
        atlases.put(atlasID, atlases.get(atlasID) + 1);
        return spriteID;
    }

    public static String stationOriginalAtlas = "/assets/stationloader/atlases/station.%s.png";
    public static String stationCopiedAtlas = "/assets/stationloader/atlases/station.%s.%s.png";
    private static boolean setup;
    private static ModTextureStatic[] textures = new ModTextureStatic[0];
    private static final Map<TextureRegistries, TreeMap<Integer, Integer>> spriteIDs = new HashMap<>();
}
