package net.modificationstation.classloader;

/**
 * Interface that is used by ClassLoader to easily access ClassTransformer's transform() method
 * 
 * @author mine_diver
 *
 */
public interface IClassTransformer
{
    /**
     * Input method for ClassTransformers that should return class's bytecode
     * 
     * @param name
     * @param bytes
     * @return
     */
    byte[] transform(String name, byte[] bytes);
}
