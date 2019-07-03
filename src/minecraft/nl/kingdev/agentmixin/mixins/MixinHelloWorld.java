package nl.kingdev.agentmixin.mixins;


import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiMainMenu.class, remap = false)
public abstract class MixinHelloWorld extends GuiScreen {

    @Inject(at = { @At(value = "RETURN") }, method = { "drawScreen" })
    public void drawScreen(int i, int j, float f, CallbackInfo info) {
        System.out.println("MIXINS IN STATIONLOADER PROOF!" + ((GuiMainMenuAccessor)this).getSplashText());
    }
}
