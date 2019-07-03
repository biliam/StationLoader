package net.modificationstation.stationloader.mixtures;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.Material;
import net.modificationstation.stationmodloader.mixture.Mixture;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene.ShiftType;

@Mixture(EntityRenderer.class)
public class EntityRendererMixture extends EntityRenderer {

	public EntityRendererMixture(Minecraft minecraft) {
		super(minecraft);
	}
	
	@Intervene(shift = ShiftType.OVERWRITE)
	private float getFOVModifier(float f) {
		EntityLiving entityliving = mc.renderViewEntity;
        float f1 = 110F;
        if(entityliving.isInsideOfMaterial(Material.water))
        {
            f1 = 60F;
        }
        if(entityliving.health <= 0)
        {
            float f2 = (float)entityliving.deathTime + f;
            f1 /= (1.0F - 500F / (f2 + 500F)) * 2.0F + 1.0F;
        }
        return f1 + prevDebugCamFOV + (debugCamFOV - prevDebugCamFOV) * f;
	}
	
	private Minecraft mc;
	private float prevDebugCamFOV;
	private float debugCamFOV;
}
