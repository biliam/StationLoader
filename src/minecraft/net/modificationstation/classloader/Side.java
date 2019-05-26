package net.modificationstation.classloader;

/**
 * Enum that represents 2 sides of Minecraft - server and client.
 * Can not be instantiated in runtime, will throw exception, even with EnumHelper.
 * 
 * @author mine_diver
 *
 */
public enum Side {
    
    CLIENT,
    SERVER;
    
    private Side() {
        try {
            values();
            new IllegalAccessException("There can't be more than 2 sides!").printStackTrace();
            System.exit(1);
        } catch (NullPointerException e) {}
    }
    
    /**
     * Returns current side of code
     * 
     * @return
     */
    public static final Side current() {
        return current;
    }
    
    /**
     * Initializes "current" variable
     * 
     * @param side
     */
    static final void setCurrent(Side side) {
        if (current == null)
            current = side;
    }
    
    private static Side current;
}