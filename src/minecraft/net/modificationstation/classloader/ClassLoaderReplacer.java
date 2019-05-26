package net.modificationstation.classloader;

import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

final class ClassLoaderReplacer
{
    static final ClassLoaderReplacer INSTANCE = new ClassLoaderReplacer();
    final MCClassLoader classLoader;
    private Object newApplet;
    private Class<? super Object> appletClass;

    private ClassLoaderReplacer()
    {
        URLClassLoader ucl = (URLClassLoader)getClass().getClassLoader();
        classLoader = new MCClassLoader(ucl.getURLs());

    }

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
