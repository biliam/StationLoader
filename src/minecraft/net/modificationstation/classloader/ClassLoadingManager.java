package net.modificationstation.classloader;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassLoadingManager {

    private void commonInit(Side sideIn) {
        if (sideIn == Side.CLIENT)
            logFile = "StationLoader_CLIENT.log";
        if (sideIn == Side.SERVER)
            logFile = "StationLoader_SERVER.log";
        Log.info("The next station is: Modification", new Object[] {});
        try {
            Field field = getClass().getDeclaredField("side");
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(this, sideIn);
        } catch (Exception e) {e.printStackTrace();}
        try {
            CoreModLoader.INSTANCE.loadMod(Class.forName("net.modificationstation.stationloader.coremod.StationCoreMod", false, ClassLoaderReplacer.INSTANCE.classLoader));
        } catch (Exception e) {System.out.println("Failed to load StationLoader core mod! Expect some errors");e.printStackTrace();}
        CoreModLoader.INSTANCE.discoverAndLoadMods();
        runPremain();
    }
    private void runPremain() {
        if (loadedCoreMods.length > 0)
            for (ICoreMod coremod : loadedCoreMods)
                coremod.premain();
    }
    public void init(String args[], Side sideIn) {
        commonInit(sideIn);
        if (side == Side.CLIENT)
            ClassLoaderReplacer.INSTANCE.relaunchClient(args);
        if (side == Side.SERVER)
            ClassLoaderReplacer.INSTANCE.relaunchServer(args);
    }
    public void init(Applet applet, boolean start) {
        commonInit(Side.CLIENT);
        if (start)
            ClassLoaderReplacer.INSTANCE.startApplet(applet);
        else
            ClassLoaderReplacer.INSTANCE.relaunchApplet(applet);
    }
    public File getMinecraftDir() {
        if (minecraftDir == null) {
            try {
                minecraftDir = new File(new File(".").getCanonicalPath());
            } catch (IOException e) {e.printStackTrace();}
        }
        return minecraftDir;
    }
    void addCoreMod(ICoreMod coremod) {
        ICoreMod prevLoadedCoreMods[] = loadedCoreMods;
        loadedCoreMods = new ICoreMod[loadedCoreMods.length + 1];
        for (int i = 0;i < prevLoadedCoreMods.length;i++)
            loadedCoreMods[i] = prevLoadedCoreMods[i];
        loadedCoreMods[prevLoadedCoreMods.length] = coremod;
    }
    private ClassLoadingManager() {}
    public static final ClassLoadingManager INSTANCE = new ClassLoadingManager();
    static String logFile;
    public final Side side = null;
    private File minecraftDir;
    private ICoreMod loadedCoreMods[] = new ICoreMod[0];
}