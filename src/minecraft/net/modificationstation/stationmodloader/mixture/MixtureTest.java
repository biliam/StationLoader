package net.modificationstation.stationmodloader.mixture;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene.ShiftType;

@Mixture(GuiMainMenu.class)
public class MixtureTest extends GuiScreen {
	
	@Override
	@Intervene(shift = ShiftType.BEFORE)
	protected void actionPerformed(GuiButton guibutton) {
		System.out.println("YOU'VE PRESSED THE " + guibutton.id + " BUTTON OF " + controlList.size() + " BUTTONS!");
	}
}
