package net.modificationstation.stationloader.util.enums;

import java.lang.reflect.Field;
import java.util.Arrays;

import net.minecraft.src.EnumToolMaterial;
import net.modificationstation.classloader.ReflectionHelper;

/**
 * EnumToolMaterial manager. You can add your own EnumToolMaterials without editing base classes.
 */

public class ToolMaterial {
    
    /**
     * This method creates and registers EnumToolMaterial and then returns is for further use.
     * 
     * @param name
     * @param id
     * @param harvestLevel
     * @param maxUses
     * @param efficiencyOnProperMaterial
     * @param damageVsEntity
     * @return
     */
    public static final EnumToolMaterial create(String name,
            int id,
            int harvestLevel,
            int maxUses,
            float efficiencyOnProperMaterial,
            int damageVsEntity)
    {
        try {
            EnumToolMaterial[] newETM = Arrays.copyOf((EnumToolMaterial[])field_21209_jField.get(null), ((EnumToolMaterial[])field_21209_jField.get(null)).length + 1);
            newETM[newETM.length - 1] = EnumHelper.addEnum(EnumToolMaterial.class, name,
                    new Class[]{
                            String.class,
                            int.class,
                            int.class,
                            int.class,
                            float.class,
                            int.class
                    }, new Object[]{
                            name,
                            id,
                            harvestLevel,
                            maxUses,
                            efficiencyOnProperMaterial,
                            damageVsEntity
                    });
            field_21209_jField.set(null, newETM);
            return ((EnumToolMaterial[])field_21209_jField.get(null))[((EnumToolMaterial[])field_21209_jField.get(null)).length - 1];
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static final Field field_21209_jField = ReflectionHelper.findField(EnumToolMaterial.class, new String[]{"j", "field_21209_j"});
    static {
        ReflectionHelper.publicField(field_21209_jField);
    }
}
