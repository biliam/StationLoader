package net.modificationstation.stationloader.util;

import net.minecraft.src.ItemStack;
import net.modificationstation.classloader.ReflectionHelper;

/**
 * The class that makes it able to access package-protected things (protected or without modifier)
 * 
 * @author mine_diver
 *
 */
public class PackageAccess {
	
	/**
	 * Minecraft access class so mods can access some private/no modifier/protected methods and fields, like Minecraft's instance
	 * 
	 * @author mine_diver
	 *
	 */
	public static class Minecraft {
		public static net.minecraft.client.Minecraft theMinecraft;
		
		static {
			try {
				theMinecraft = (net.minecraft.client.Minecraft) ReflectionHelper.findField(net.minecraft.client.Minecraft.class, new String[] {"a", "theMinecraft"}).get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
    
    /**
     * CraftingManager access class so mods can register recipes (original CraftingManager has functions without modifiers
     * so we can't access it outside net.minecraft.src)
     * 
     * @author mine_diver
     *
     */
	public static class CraftingManager {
		public static final CraftingManager getInstance() {
			return instance;
		}
		public void addRecipe(ItemStack itemstack, Object aobj[]) {
			try {
				net.minecraft.src.CraftingManager cf = net.minecraft.src.CraftingManager.getInstance();
				ReflectionHelper.findMethod(net.minecraft.src.CraftingManager.class, cf, new String[] {"a", "addRecipe"}, ItemStack.class, Object[].class).invoke(cf, itemstack, aobj);
			} catch (Exception e) {e.printStackTrace();}
		}
		private CraftingManager() {}
		private static final CraftingManager instance = new CraftingManager();
	}
}
