package net.modificationstation.classloader;

import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * Class that relaunches Minecraft under new ClassLoader that we can control
 * 
 * @author mine_diver
 * 
 */
public final class ClassLoaderReplacer
{
    public static final ClassLoaderReplacer INSTANCE = new ClassLoaderReplacer();
    public final MCClassLoader classLoader;
    private Object newApplet;
    private Class<? super Object> appletClass;

    private ClassLoaderReplacer()
    {
        URLClassLoader ucl = (URLClassLoader)getClass().getClassLoader();
        classLoader = new MCClassLoader(ucl.getURLs());

    }
    
    /**
     * Method that is used to relaunch client under our ClassLoader if it was launched from Minecraft.class
     * Deprecation: using deprecated Thread.currentThread().stop(); method, because we need to stop the main thread from executing,
     * as the relaunched Minecraft runs in its own thread and we don't need 2 Minecrafts. 
     * 
     * @param args
     */
    @SuppressWarnings("deprecation")
    final void relaunchClient(String args[])
    {
        File minecraftHome = computeExistingClientHome();
        Class<? super Object> client = ReflectionHelper.getClass(classLoader, "net.minecraft.client.Minecraft");
        ReflectionHelper.setPrivateValue(client, null, minecraftHome, "minecraftDir", "af", "minecraftDir");
        try
        {
            ReflectionHelper.findMethod(client, null, new String[] { "main" }, String[].class).invoke(null, new Object[] {args});
            Thread.currentThread().stop();
        }
        catch (Exception e) {e.printStackTrace();}
    }
    
    /**
     * Relaunches server. Still being worked though, because server side isn't currently supported
     * 
     * @param args
     */
    @SuppressWarnings("deprecation")
    final void relaunchServer(String args[])
    {
        try
        {
            Class<? super Object> server = ReflectionHelper.getClass(classLoader, "net.minecraft.server.MinecraftServer");
            ReflectionHelper.findMethod(server, null, new String[] { "actualMain" }, String[].class).invoke(null, (Object[])args);
            Thread.currentThread().stop();
        }
        catch (Exception e) {e.printStackTrace();}
    }
    
    /**
     * Using the Minecraft.class in the original ClassLoader this method gets .minecraft dir path
     * 
     * @return
     */
    private final File computeExistingClientHome()
    {
        Class<? super Object> mcMaster = ReflectionHelper.getClass(getClass().getClassLoader(), "net.minecraft.client.Minecraft");
        Method setupHome = ReflectionHelper.findMethod(mcMaster, null, new String[] {"getMinecraftDir", "b"} );
        try
        {
            setupHome.invoke(null);
        }
        catch (Exception e)
        {
            
        }
        File minecraftHome = ReflectionHelper.getPrivateValue(mcMaster, null, "minecraftDir", "af", "minecraftDir");
        return minecraftHome;
    }
    
    /**
     * Usually Minecraft launchers use MinecraftApplet.class to start the game instead of Minecraft.class, so this method
     * actually restarts MinecraftApplet from the launcher itself under our ClassLoader
     * 
     * @param minecraftApplet
     */
    final void relaunchApplet(Applet minecraftApplet)
    {
        appletClass = ReflectionHelper.getClass(classLoader, "net.minecraft.client.MinecraftApplet");
        if (minecraftApplet.getClass().getClassLoader() == classLoader)
        {
            try
            {
                newApplet = minecraftApplet;
                ReflectionHelper.findMethod(appletClass, newApplet, new String[] {"init"}).invoke(newApplet);
                return;
            }
            catch (Exception e)
            {
                System.out.println("ClassLoaderReplacer.relaunchApplet");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        Class<? super Object> parentAppletClass = ReflectionHelper.getClass(getClass().getClassLoader(), "java.applet.Applet");
        try
        {
            newApplet = appletClass.newInstance();
            Object appletContainer = ReflectionHelper.getPrivateValue(ReflectionHelper.getClass(getClass().getClassLoader(), "java.awt.Component"), minecraftApplet, "parent");

            Class<? super Object> launcherClass = ReflectionHelper.getClass(getClass().getClassLoader(), "net.minecraft.Launcher");
            if (launcherClass.isInstance(appletContainer))
            {
                ReflectionHelper.findMethod(ReflectionHelper.getClass(getClass().getClassLoader(), "java.awt.Container"), minecraftApplet, new String[] { "removeAll" }).invoke(appletContainer);
                ReflectionHelper.findMethod(launcherClass, appletContainer, new String[] { "replace" }, parentAppletClass).invoke(appletContainer, newApplet);
            }
            else
            {
                Log.severe("Found unknown applet parent %s, unable to inject!\n", launcherClass);
                throw new RuntimeException();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * After Minecraft applet init() method is called, there is start() method next, so we need to relaunch it too
     * 
     * @param applet
     */
    final void startApplet(Applet applet)
    {
        if (applet.getClass().getClassLoader() == classLoader)
        {
            try
            {
                ReflectionHelper.findMethod(appletClass, newApplet, new String[] {"start"}).invoke(newApplet);
            }
            catch (Exception e)
            {
                System.out.println("ClassLoaderReplacer.startApplet");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return;
    }
}
