package net.modificationstation.classloader;

/**
 * Interface that is used by CoreModLoader to identify core mods in a jar
 * 
 * @author mine_diver
 *
 */
public interface ICoreMod
{
    /**
     * premain() method is used to initialize core mods (for example, on premain coremods can register ClassTransformers)
     */
    void premain();
}
