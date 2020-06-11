package net.modificationstation.stationloader.client.textures;

public enum TextureRegistries {

    TERRAIN("/terrain.png", 256),
    GUI_ITEMS("/gui/items.png", 256);

    TextureRegistries(String path, int texturesPerFile) {
        this.path = path;
        this.texturesPerFile = Integer.toUnsignedLong(texturesPerFile);
    }

    public String path() {
        return path;
    }

    public int texturesPerFile() {
        return (int) texturesPerFile;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTexturesPerFile(int texturesPerFile) {
        this.texturesPerFile = Integer.toUnsignedLong(texturesPerFile);
    }

    @Override
    public String toString() {
        return path();
    }

    private String path;
    private long texturesPerFile;
}
