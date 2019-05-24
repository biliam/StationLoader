package net.modificationstation.classloader;

import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

class ClassLoaderReplacer
{
    static final ClassLoaderReplacer INSTANCE = new ClassLoaderReplacer();
    private static Side side;
    MCClassLoader classLoader;
    private Object newApplet;
    private Class<? super Object> appletClass;

    JDialog popupWindow;

    private ClassLoaderReplacer()
    {
        URLClassLoader ucl = (URLClassLoader)getClass().getClassLoader();

        classLoader = new MCClassLoader(ucl.getURLs());

    }

    @SuppressWarnings("deprecation")
    void relaunchClient(String args[])
    {
        // Now we re-inject the home into the "new" minecraft under our control
        Class<? super Object> client;
        try
        {
            File minecraftHome = computeExistingClientHome();
            setupHome(minecraftHome);

            client = ReflectionHelper.getClass(classLoader, "net.minecraft.client.Minecraft");
            ReflectionHelper.setPrivateValue(client, null, minecraftHome, "minecraftDir", "af", "minecraftDir");
        }
        finally
        {
            if (popupWindow!=null)
            {
                popupWindow.setVisible(false);
                popupWindow.dispose();
            }
        }

        try
        {
            ReflectionHelper.findMethod(client, null, new String[] { "main" }, String[].class).invoke(null, new Object[] {args});
            Thread.currentThread().stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // Hmmm
        }
    }

    @SuppressWarnings("deprecation")
    void relaunchServer(String args[])
    {
        // Now we re-inject the home into the "new" minecraft under our control
        Class<? super Object> server;
        File minecraftHome = new File(".");
        setupHome(minecraftHome);

        server = ReflectionHelper.getClass(classLoader, "net.minecraft.server.MinecraftServer");
        try
        {
            ReflectionHelper.findMethod(server, null, new String[] { "actualMain" }, String[].class).invoke(null, (Object[])args);
            Thread.currentThread().stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupHome(File minecraftHome)
    {
        //FMLInjectionData.build(minecraftHome, classLoader);
        //Log.info("Forge Mod Loader version %s.%s.%s.%s for Minecraft client:%s, server:%s loading", FMLInjectionData.major, FMLInjectionData.minor, FMLInjectionData.rev, FMLInjectionData.build, FMLInjectionData.mccversion, FMLInjectionData.mcsversion);

        try
        {
            //RelaunchLibraryManager.handleLaunch(minecraftHome, classLoader);
        }
        catch (Throwable t)
        {
            if (popupWindow != null)
            {
                try
                {
                    String logFile = new File(minecraftHome,"ForgeModLoader-client-0.log").getCanonicalPath();
                    JOptionPane.showMessageDialog(popupWindow,
                            String.format("<html><div align=\"center\"><font size=\"+1\">There was a fatal error starting up minecraft and FML</font></div><br/>" +
                            		"Minecraft cannot launch in it's current configuration<br/>" +
                            		"Please consult the file <i><a href=\"file:///%s\">%s</a></i> for further information</html>", logFile, logFile
                            		), "Fatal FML error", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception ex)
                {
                    // ah well, we tried
                }
            }
            throw new RuntimeException(t);
        }
    }

    /**
     * @return
     */
    private File computeExistingClientHome()
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

    void relaunchApplet(Applet minecraftApplet)
    {
        appletClass = ReflectionHelper.getClass(classLoader, "net.minecraft.client.MinecraftApplet");
        if (minecraftApplet.getClass().getClassLoader() == classLoader)
        {
            try
            {
                newApplet = minecraftApplet;
                ReflectionHelper.findMethod(appletClass, newApplet, new String[] {"fmlInitReentry"}).invoke(newApplet);
                return;
            }
            catch (Exception e)
            {
                System.out.println("FMLRelauncher.relaunchApplet");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        setupHome(computeExistingClientHome());

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
        finally
        {
            if (popupWindow!=null)
            {
                popupWindow.setVisible(false);
                popupWindow.dispose();
            }
        }
    }

    public static void appletStart(Applet applet)
    {
        INSTANCE.startApplet(applet);
    }

    void startApplet(Applet applet)
    {
        if (applet.getClass().getClassLoader() == classLoader)
        {
            try
            {
                ReflectionHelper.findMethod(appletClass, newApplet, new String[] {"fmlStartReentry"}).invoke(newApplet);
            }
            catch (Exception e)
            {
                System.out.println("FMLRelauncher.startApplet");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return;
    }

    public static Side side()
    {
        return side;
    }
}
