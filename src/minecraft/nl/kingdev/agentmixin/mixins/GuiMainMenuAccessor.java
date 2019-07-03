package nl.kingdev.agentmixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.src.GuiMainMenu;

@Mixin(GuiMainMenu.class)
public interface GuiMainMenuAccessor {
	
	@Accessor
	String getSplashText();
	
}
