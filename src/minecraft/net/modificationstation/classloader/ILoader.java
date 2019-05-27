package net.modificationstation.classloader;

/**
 * Interface for loaders (LibraryLoader, CoreModLoader, etc)
 * 
 * @author mine_diver
 *
 */
public interface ILoader {
    
    /**
     * This method is used to find loader's targets and load them
     */
    void discoverAndLoad();
}
