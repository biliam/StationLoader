package net.modificationstation.stationloader.proxy;

import net.modificationstation.stationloader.client.gui.GuiHandler;
import net.modificationstation.stationloader.events.EventsManager;
import net.modificationstation.stationmodloader.events.MCInitializationEvent;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;
import net.modificationstation.stationmodloader.transformers.MixtureTransformer;

/**
 * ClientProxy for StationLoader that registers all client-side-only stuff, like GuiHandler to customize GuiMainMenu
 * 
 * @author mine_diver
 *
 */
public final class ClientProxy extends CommonProxy {
	
	@Override
	public final void preInit(MCPreInitializationEvent event) {
		super.preInit(event);
		MixtureTransformer.registerMixture("net.modificationstation.stationloader.mixtures.EntityRendererMixture");
		MixtureTransformer.registerMixture("net.modificationstation.stationloader.mixtures.GuiScreenMixture");
	}
	
	@Override
	public final void init(MCInitializationEvent event) {
		super.init(event);
		EventsManager.register(new GuiHandler());
	}
}
