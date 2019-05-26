package net.modificationstation.classloader;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;

public final class ClassLoadingManager {

    private final void commonInit(Side sideIn) {
        init = true;
        Side.setCurrent(sideIn);
        if (Side.current() == Side.CLIENT)
            logFile = "StationLoader_CLIENT.log";
        if (Side.current() == Side.SERVER)
            logFile = "StationLoader_SERVER.log";
        Log.info("Loading libraries");
        LibrariesLoader.INSTANCE.discoverAndLoad();
        Log.info("Finished loading libraries");
        Log.info("Loading core mods");
        try {
            ((CoreModLoader)CoreModLoader.INSTANCE).loadMod(Class.forName("net.modificationstation.stationloader.coremod.StationCoreMod", false, ClassLoaderReplacer.INSTANCE.classLoader));
        } catch (Exception e) {System.out.println("Failed to load StationLoader core mod! Skipping");e.printStackTrace();}
        CoreModLoader.INSTANCE.discoverAndLoad();
        Log.info("Finished loading core mods");
        Log.info("Running premains");
        runPremain();
        Log.info("Finished premains");
        Log.info("Starting Minecraft...");
    }
    private final void runPremain() {
        if (loadedCoreMods.length > 0)
            for (ICoreMod coremod : loadedCoreMods)
                coremod.premain();
    }
    public final void init(String args[], Side sideIn) {
        if (!init) {
            commonInit(sideIn);
            if (Side.current() == Side.CLIENT)
                ClassLoaderReplacer.INSTANCE.relaunchClient(args);
            if (Side.current() == Side.SERVER)
                ClassLoaderReplacer.INSTANCE.relaunchServer(args);
        }
    }
    public final void init(Applet applet, boolean start) {
        if (!init) {
            commonInit(Side.CLIENT);
            if (start)
                ClassLoaderReplacer.INSTANCE.startApplet(applet);
            else
                ClassLoaderReplacer.INSTANCE.relaunchApplet(applet);
        }
    }
    public final File getMinecraftDir() {
        if (minecraftDir == null) {
            try {
                minecraftDir = new File(new File(".").getCanonicalPath());
            } catch (IOException e) {e.printStackTrace();}
        }
        return minecraftDir;
    }
    final void addCoreMod(ICoreMod coremod) {
        ICoreMod prevLoadedCoreMods[] = loadedCoreMods;
        loadedCoreMods = new ICoreMod[loadedCoreMods.length + 1];
        for (int i = 0;i < prevLoadedCoreMods.length;i++)
            loadedCoreMods[i] = prevLoadedCoreMods[i];
        loadedCoreMods[prevLoadedCoreMods.length] = coremod;
    }
    private ClassLoadingManager() {}
    public static final ClassLoadingManager INSTANCE = new ClassLoadingManager();
    private static boolean init = false;
    static String logFile;
    private File minecraftDir;
    private ICoreMod loadedCoreMods[] = new ICoreMod[0];
}