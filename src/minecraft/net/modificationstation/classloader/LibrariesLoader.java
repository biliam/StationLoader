package net.modificationstation.classloader;

import java.io.File;
import java.net.MalformedURLException;

final class LibrariesLoader implements ILoader{

    @Override
    public final void discoverAndLoad() {
        for (File file : new File(ClassLoadingManager.INSTANCE.getMinecraftDir(), "/lib/").listFiles()) {
            Log.info("Found a file (%s)", file.getName());
            if (file.getName().endsWith(".jar"))
                try {
                    Log.info("Loading \"%s\" library", file.getName().substring(0, file.getName().length() - 4));
                    loadLib(file);
                    Log.info("Successfully loaded");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
        }
    }
    
    final void loadLib(File file) throws MalformedURLException {
        ClassLoaderReplacer.INSTANCE.classLoader.addURL(file.toURI().toURL());
    }
    
    private LibrariesLoader() {}
    static final ILoader INSTANCE = new LibrariesLoader();
}
