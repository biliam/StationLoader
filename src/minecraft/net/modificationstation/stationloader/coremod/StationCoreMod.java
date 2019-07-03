package net.modificationstation.stationloader.coremod;

import net.modificationstation.classloader.ICoreMod;
import net.modificationstation.classloader.MCClassLoader;
import net.modificationstation.stationmodloader.transformers.MixtureTransformer;

/**
 * StationLoader's coremod that provides some class transformers
 * 
 * @author mine_diver
 *
 */
public class StationCoreMod implements ICoreMod {
    
    
    @Override
    public void premain() {
        ((MCClassLoader)getClass().getClassLoader()).registerTransformer("net.modificationstation.stationloader.coremod.EventsInjectorTransformer");
        ((MCClassLoader)getClass().getClassLoader()).registerTransformer("net.modificationstation.stationloader.coremod.SideTransformer");
    }
    
}
