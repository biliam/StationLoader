package net.modificationstation.stationloader.client.textures;

import net.minecraft.src.RenderEngine;
import net.modificationstation.stationloader.event.client.textures.TexturesPerFileListener;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum TextureRegistries {

    TERRAIN("/terrain.png", 256),
    PARTICLES("/particles.png", 1024),
    GUI_ITEMS("/gui/items.png", 256),
    GUI_PARTICLES("/gui/particles.png", 1024);

    TextureRegistries(String path, int texturesPerFile) {
        this.texturesPerFile = (short) texturesPerFile;
        addAtlas(path);
    }

    public int texturesPerFile() {
        return Short.toUnsignedInt(texturesPerFile);
    }

    public String getAtlas(int ID) {
        return atlasIDToPath.get(ID);
    }

    public Integer getAtlasID(String path) {
        return atlasPathToID.get(path);
    }

    public int addAtlas(String atlas) {
        atlas = String.format(atlas, nextAtlasID);
        atlasPathToID.put(atlas, nextAtlasID);
        atlasIDToPath.put(nextAtlasID, atlas);
        int ret = nextAtlasID;
        nextAtlasID++;
        return ret;
    }

    public void setTexturesPerFile(int texturesPerFile) {
        if (this.texturesPerFile != (short) texturesPerFile) {
            this.texturesPerFile = (short) texturesPerFile;
            TexturesPerFileListener.EVENT.getInvoker().texturesPerFileChanged(this);
        }
    }

    public int getAtlasTexture(RenderEngine renderEngine, int ID) {
        return renderEngine.getTexture(getAtlas(ID));
    }

    public void bindAtlas(RenderEngine renderEngine, int ID) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getAtlasTexture(renderEngine, ID));
        currentTexture = ID;
        if (currentBindRegistry != this)
            currentBindRegistry = this;
    }

    public static void unbind() {
        currentBindRegistry = null;
    }

    public int currentTexture() {
        return currentTexture;
    }

    public static TextureRegistries currentBindRegistry() {
        return currentBindRegistry;
    }

    @Override
    public String toString() {
        return name() + ", " + Arrays.toString(atlasPathToID.values().toArray()) + ", textures per file: " + texturesPerFile;
    }

    private static TextureRegistries currentBindRegistry;
    private final Map<Integer, String> atlasIDToPath = new HashMap<>();
    private final Map<String, Integer> atlasPathToID = new HashMap<>();
    private int nextAtlasID;
    private short texturesPerFile;
    private int currentTexture;
}
