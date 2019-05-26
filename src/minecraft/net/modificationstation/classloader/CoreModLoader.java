package net.modificationstation.classloader;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class CoreModLoader implements ILoader {
    public final void discoverAndLoad() {
        File modsFolder = null;
        modsFolder = new File(ClassLoadingManager.INSTANCE.getMinecraftDir() + "/mods/");
        for (int j = new File(ClassLoadingManager.INSTANCE.getMinecraftDir() + "/mods/b1.7.3").exists() ? 0 : 1; j < 2;j++) {
            if (!modsFolder.exists()) {modsFolder.mkdir();}
            File[] mods = modsFolder.listFiles();
            for (int i = 0; i < mods.length; i++){
                File modFile = mods[i];
                if (!modFile.isDirectory() && modFile.toString().endsWith(".jar")) {
                    Log.info("Found a potential core mod jar (%s)", modFile.getName());
                    try {
                        ClassLoaderReplacer.INSTANCE.classLoader.addURL(modFile.toURI().toURL());
                        Log.info("Added in classloader");
                        JarFile modJar = new JarFile(modFile);
                        Enumeration<JarEntry> modClasses = modJar.entries();
                        while (modClasses.hasMoreElements()) {
                            JarEntry modClass = modClasses.nextElement();
                            try {
                                Class<?> clazz = Class.forName(modClass.getName().replace(".class", "").replace("/", "."), false, ClassLoaderReplacer.INSTANCE.classLoader);
                                for (Class<?> interf : clazz.getInterfaces())
                                    if (interf.equals(ICoreMod.class)) {
                                        Log.info("Trying to load %s core mod", clazz.getName());
                                        try {
                                            loadMod(clazz);
                                            Log.info("Successfully loaded");
                                        } catch (Exception e) {e.printStackTrace();}
                                    }
                            } catch (Exception e) {}
                        }
                        modJar.close();
                    } catch (Exception e) {e.printStackTrace();}
                }
            }
            modsFolder = new File(ClassLoadingManager.INSTANCE.getMinecraftDir() + "/mods/b1.7.3");
        }
    }
    final void loadMod(Class<?> coremod) throws InstantiationException, IllegalAccessException {
        ClassLoadingManager.INSTANCE.addCoreMod(ICoreMod.class.cast(coremod.newInstance()));
    }
    private CoreModLoader() {}
    static final ILoader INSTANCE = new CoreModLoader();
}
