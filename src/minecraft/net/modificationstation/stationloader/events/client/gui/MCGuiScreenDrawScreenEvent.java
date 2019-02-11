package net.modificationstation.stationloader.events.client.gui;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.ICancellable;
import net.modificationstation.stationloader.events.common.MCEvent;

public class MCGuiScreenDrawScreenEvent extends MCEvent implements ICancellable{

    public MCGuiScreenDrawScreenEvent(GuiScreen guiscreen, int i, int j, float f, String type) {
        eventData = new Object[] {guiscreen, i, j, f, type};
    }
    @Override
    public boolean continueExecution() {
        return !cancelled;
    }
    @Override
    public void cancel() {
        cancelled = true;
    }
    public GuiScreen getGuiScreen() {
        return (GuiScreen) eventData[0];
    }
    public int getX() {
        return (Integer) eventData[1];
    }
    public int getY() {
        return (Integer) eventData[2];
    }
    public float getPartialTicks() {
        return (Float) eventData[3];
    }
    public String getScreenType() {
        return (String) eventData[4];
    }
    private Object eventData[];
    private boolean cancelled = false;
}
