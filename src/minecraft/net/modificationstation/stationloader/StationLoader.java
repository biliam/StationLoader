package net.modificationstation.stationloader;

import java.util.logging.Logger;

import net.modificationstation.stationloader.proxy.CommonProxy;
import net.modificationstation.stationmodloader.events.MCPostInitializationEvent;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;
import net.modificationstation.stationmodloader.util.Mod;
import net.modificationstation.stationmodloader.util.Mod.EventHandler;
import net.modificationstation.stationmodloader.util.Mod.Instance;
import net.modificationstation.stationmodloader.util.Mod.SidedProxy;

/**
 * StationLoader's mod (it's loaded with StationModLoader)
 * 
 * @author mine_diver
 *
 */
@Mod(name = Reference.NAME, modid = Reference.MODID, version = Reference.VERSION)
public class StationLoader {
	
	@SidedProxy(serverSide = "net.modificationstation.stationloader.proxy.ServerProxy", clientSide = "net.modificationstation.stationloader.proxy.ClientProxy")
	private final CommonProxy PROXY = null;
	
	@Instance
	public static final StationLoader INSTANCE = null;
	
	public final Logger LOGGER = null;
	
	@EventHandler
	private final void preInit(MCPreInitializationEvent event) {
		PROXY.preInit(event);
	}
}
