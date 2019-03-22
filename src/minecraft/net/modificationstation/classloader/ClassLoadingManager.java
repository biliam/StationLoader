package net.modificationstation.classloader;

public class ClassLoadingManager {
    public static void init() {
        System.out.println("init");
    }
    static {
        System.out.println("static");
    }
}
