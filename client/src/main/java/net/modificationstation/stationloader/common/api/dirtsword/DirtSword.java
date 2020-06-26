package net.modificationstation.stationloader.common.api.dirtsword;


import java.io.InputStream;

import org.lwjgl.opengl.GL11;

public class DirtSword {

	public DirtSword() {
		
	}
	public void Loader(InputStream inputStreamObj) {
		//InputStream inputStreamObj = DirtSword.class.getResourceAsStream(File.separator + "DirtSwordModels" + File.separator + objPath);
		OBJLoader load = new OBJLoader();
		OBJ loaded = load.loadModel(inputStreamObj);
		load.render(loaded);
	}
	public void Loader(InputStream inputStreamObj, Double x, Double y, Double z) {
		//InputStream inputStreamObj = DirtSword.class.getResourceAsStream(File.separator + "DirtSwordModels" + File.separator + objPath);
		OBJLoader load = new OBJLoader();
		GL11.glPushMatrix();
    	GL11.glTranslated(x, y, z);
		OBJ loaded = load.loadModel(inputStreamObj);
		load.render(loaded);
		GL11.glPopMatrix();
		
	}
}
