package net.modificationstation.stationmodloader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.modificationstation.classloader.ICoreMod;
import net.modificationstation.classloader.Log;
import net.modificationstation.stationmodloader.loaders.Loader;

public class StationModLoader implements ICoreMod{
    
	public static void init() {
		try {
			((Loader)Loader.INSTANCE).loadMod(Class.forName("net.modificationstation.stationloader.StationLoader"), INSTANCE.getClass().getClassLoader());
		} catch (Exception e) {LOGGER.warning("Failed to load StationLoader! Skipping");e.printStackTrace();};
		Loader.INSTANCE.discoverAndLoad();
	}
	public static void addMod(Object mod){
		if(!loadedMods.contains(mod)){
			loadedMods.add(mod);
			LOGGER.info("Added \"" + mod.getClass().getName() +"\" mod");
		}
	}
    @Override
    public void premain() {
    }
    
    private static final StationModLoader INSTANCE = new StationModLoader();
	public static final Logger LOGGER = Logger.getLogger("StationModLoader");
	public static final List<Object> loadedMods = new ArrayList<Object>();
	static {
        LOGGER.setParent(Log.log.getLogger());
	}
}
