package net.modificationstation.classloader;

public interface IClassTransformer
{
    byte[] transform(String name, byte[] bytes);
}
