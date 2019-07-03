package net.modificationstation.stationloader.mixtures;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.client.gui.guiscreen.DrawScreen;
import net.modificationstation.stationmodloader.mixture.Mixture;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene.ShiftType;

@Mixture(GuiScreen.class)
public class GuiScreenMixture extends GuiScreen {
	
	@Override
	@Intervene(shift = ShiftType.BEFORE)
	public void drawScreen(int i, int j, float f) {
		if (!DrawScreen.EVENT.getInvoker().drawScreen(this, i, j, f, "GuiScreen"))
			return;
	}
}
