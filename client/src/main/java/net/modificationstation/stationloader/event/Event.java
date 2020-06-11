package net.modificationstation.stationloader.event;

/**
 * Abstract event class
 * 
 * @author mine_diver
 *
 * @param <T>
 **/

public abstract class Event<T> {

    public T getInvoker() {
    	return invoker;
    }

    public abstract void register(T listener);
    protected T invoker;
}
