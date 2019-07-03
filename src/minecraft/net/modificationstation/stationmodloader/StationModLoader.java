package net.modificationstation.stationmodloader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.modificationstation.classloader.ICoreMod;
import net.modificationstation.classloader.Log;
import net.modificationstation.classloader.MCClassLoader;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;
import net.modificationstation.stationmodloader.loaders.Loader;
import net.modificationstation.stationmodloader.transformers.MixtureTransformer;

public class StationModLoader implements ICoreMod {
	
	public static void addMod(Object mod){
		if(!loadedMods.contains(mod)){
			loadedMods.add(mod);
			LOGGER.info("Added \"" + mod.getClass().getName() +"\" mod");
		}
	}
    @Override
    public void premain() {
    	MCClassLoader cl = (MCClassLoader) getClass().getClassLoader();
    	cl.registerTransformer("net.modificationstation.stationmodloader.transformers.EventsInjectorTransformer");
    	cl.registerTransformer("net.modificationstation.stationmodloader.transformers.MixtureTransformer");
    	try {
			((Loader)Loader.INSTANCE).loadMod(Class.forName("net.modificationstation.stationloader.StationLoader"), cl);
		} catch (Exception e) {LOGGER.warning("Failed to load StationLoader! Skipping");e.printStackTrace();};
		Loader.INSTANCE.discoverAndLoad();
		new MCPreInitializationEvent().process();
    }
    
    //private static final StationModLoader INSTANCE = new StationModLoader();
	public static final Logger LOGGER = Logger.getLogger("StationModLoader");
	public static final List<Object> loadedMods = new ArrayList<Object>();
	static {
        LOGGER.setParent(Log.log.getLogger());
	}
}
