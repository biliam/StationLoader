package net.modificationstation.stationloader.proxy;
import net.modificationstation.stationloader.client.gui.GuiHandler;
import net.modificationstation.stationloader.events.common.EventsManager;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;

/**
 * ClientProxy for StationLoader that registers all client-side-only stuff, like GuiHandler to customize GuiMainMenu
 * 
 * @author mine_diver
 *
 */
public class ClientProxy extends CommonProxy{
	@Override
	public void preInit(MCPreInitializationEvent event) {
		EventsManager.register(new GuiHandler());
		super.preInit(event);
	}
}
