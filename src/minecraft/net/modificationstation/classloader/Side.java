package net.modificationstation.classloader;

public enum Side {
    CLIENT,
    SERVER;
    
    private Side() {
        if (!name().equals("CLIENT") && !name().equals("SERVER"))
            try {
                throw new IllegalAccessException("There can't be more than 2 sides!");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.exit(1);
            }
    }
}