package net.modificationstation.stationloader.client.gui;

import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.StationLoader;
import net.modificationstation.stationloader.events.client.gui.guiscreen.DrawScreen;
import net.modificationstation.stationloader.util.PackageAccess;
import net.modificationstation.stationmodloader.StationModLoader;
import net.modificationstation.stationmodloader.util.Mod;

/**
 * This class handles DrawScreen GUI event for StationLoader
 * 
 * On DrawScreen it checks if it's GuiMainMenu and if event is called from super class GuiScreen
 * (we need exactly super class GuiScreen because it's called when screen wants to render buttons,
 *  or in other words after the most render is done)
 *  and if it's, GuiHandler draws two strings under "Minecraft Beta 1.7.3" with StationLoader's version
 *  and the count of loaded mods (excluding StationLoader)
 * 
 * @author mine_diver
 *
 */
public class GuiHandler implements DrawScreen {
	@Override
	public boolean drawScreen(GuiScreen guiscreen, int x, int y, float partialTicks, String screenType) {
		if (guiscreen instanceof GuiMainMenu && screenType.equals("GuiScreen")) {
			guiscreen.drawString(PackageAccess.Minecraft.theMinecraft.fontRenderer, "StationLoader version " + StationLoader.class.getAnnotation(Mod.class).version(), 2, 12, 0x505050);
			guiscreen.drawString(PackageAccess.Minecraft.theMinecraft.fontRenderer, "(" + (StationModLoader.loadedMods.size() - 1 > 1 ? (StationModLoader.loadedMods.size() - 1) + " mods are" : StationModLoader.loadedMods.size() - 1 == 1 ? "1 mod is" : "No mods are") + " loaded)", 2, 22, 0x505050);
        }
		return true;
	}
}
