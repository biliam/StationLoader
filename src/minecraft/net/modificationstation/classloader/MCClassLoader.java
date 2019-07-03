package net.modificationstation.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.modificationstation.stationmodloader.transformers.MixtureTransformer;

/**
 * Minecraft ClassLoader
 * 
 * @author mine_diver
 *
 */
public class MCClassLoader extends URLClassLoader
{
    private static final String[] excludedPackages = {
        //"java.", "sun.", "javax.",
        "net.modificationstation.classloader."
    };

    private static final String[] transformerExclusions =
    {
        "org.objectweb.asm."
    };
    private final List<URL> sources;
    private final ClassLoader parent;

    public final List<IClassTransformer> transformers;
    private final Map<String, Class<?>> cachedClasses;

    MCClassLoader(URL[] sources)
    {
        super(sources, null);
        this.sources = new ArrayList<URL>(Arrays.asList(sources));
        this.parent = getClass().getClassLoader();
        this.cachedClasses = new HashMap<String,Class<?>>(1000);
        this.transformers = new ArrayList<IClassTransformer>(2);
        ReflectionHelper.setPrivateValue(ClassLoader.class, null, this, "scl");
        Thread.currentThread().setContextClassLoader(this);
        //registerTransformer("net.modificationstation.stationmodloader.transformers.MixtureTransformer");
    }
    
    /**
     * Using this method core mods can provide ClassTransformers for ClassLoader
     * 
     * @param transformerClassName
     */
    public final void registerTransformer(String transformerClassName)
    {
        try
        {
            transformers.add((IClassTransformer) loadClass(transformerClassName).newInstance());
        }
        catch (Exception e)
        {
            Log.log(Level.SEVERE, e, "A critical problem occured registering the ASM transformer class %s", transformerClassName);
        }
    }
    
    /**
     * Using this method we can store some classes in cache and run ClassTransformers, as well as not run ClassTransformers on
     * transformer exclusions
     */
    @Override
    public final Class<?> findClass(String name) throws ClassNotFoundException
    {
        for (String st : excludedPackages)
        {
            if (name.startsWith(st))
            {
                return parent.loadClass(name);
            }
        }

        if (cachedClasses.containsKey(name))
        {
            return cachedClasses.get(name);
        }

        for (String st : transformerExclusions)
        {
            if (name.startsWith(st))
            {
                Class<?> cl = super.findClass(name);
                cachedClasses.put(name, cl);
                return cl;
            }
        }

        try
        {
            int lastDot = name.lastIndexOf('.');
            if (lastDot > -1)
            {
                String pkgname = name.substring(0, lastDot);
                if (getPackage(pkgname)==null)
                {
                    definePackage(pkgname, null, null, null, null, null, null, null);
                }
            }
            byte[] basicClass = getClassBytes(name);
            byte[] transformedClass = runTransformers(name, basicClass);
            Class<?> cl = defineClass(name, transformedClass, 0, transformedClass.length);
            cachedClasses.put(name, cl);
            return cl;
        }
        catch (Throwable e)
        {
            throw new ClassNotFoundException(name, e);
        }
    }
    
    /**
     * Returns class's bytecode for further transformation
     * 
     * @param name
     * @return
     * @throws IOException
     */
    public final byte[] getClassBytes(String name) throws IOException
    {
        InputStream classStream = null;
        try
        {
            URL classResource = findResource(name.replace('.', '/').concat(".class"));
            if (classResource == null)
            {
                return null;
            }
            classStream = classResource.openStream();
            return readFully(classStream);
        }
        finally
        {
            if (classStream != null)
            {
                try
                {
                    classStream.close();
                }
                catch (IOException e) {}
            }
        }
    }
    
    /**
     * Runs ClassTransformers to transform a class that is about to be loaded
     * 
     * @param name
     * @param basicClass
     * @return
     */
    private final byte[] runTransformers(String name, byte[] basicClass)
    {
        for (IClassTransformer transformer : transformers)
        {
            basicClass = transformer.transform(name, basicClass);
        }
        return basicClass;
    }
    
    /**
     * Adds a file into ClassLoader sources
     */
    @Override
    public final void addURL(URL url)
    {
        super.addURL(url);
        sources.add(url);
    }
    
    /**
     * Returns all loaded sources
     * 
     * @return
     */
    public final List<URL> getSources()
    {
        return sources;
    }

    /**
     * Reads byte files. Usually used by getClassBytes() method
     * 
     * @param stream
     * @return
     */
    private final byte[] readFully(InputStream stream)
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(stream.available());
            int r;
            while ((r = stream.read()) != -1)
            {
                bos.write(r);
            }

            return bos.toByteArray();
        }
        catch (Throwable t)
        {
            return new byte[0];
        }
    }
}
