package net.modificationstation.stationloader.events.common;

public interface ICancellable {
    public boolean continueExecution();
    public void cancel();
}