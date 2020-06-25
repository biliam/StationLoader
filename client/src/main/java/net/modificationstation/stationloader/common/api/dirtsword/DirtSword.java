package net.modificationstation.stationloader.common.api.dirtsword;

import java.io.InputStream;

import org.lwjgl.opengl.GL11;

public class DirtSword {

	public DirtSword() {
		
	}
	public void Loader(InputStream inputStreamObj) {
		//InputStream inputStreamObj = DirtSword.class.getResourceAsStream(File.separator + "DirtSwordModels" + File.separator + objPath);
		ObjLoader load = new ObjLoader();
		Obj loaded = load.loadModel(inputStreamObj);
		load.render(loaded);
	}
	public void Loader(InputStream inputStreamObj, Double x, Double y, Double z) {
		//InputStream inputStreamObj = DirtSword.class.getResourceAsStream(File.separator + "DirtSwordModels" + File.separator + objPath);
		ObjLoader load = new ObjLoader();
		GL11.glPushMatrix();
    	GL11.glTranslated(x, y, z);
		Obj loaded = load.loadModel(inputStreamObj);
		load.render(loaded);
		GL11.glPopMatrix();
		
	}
}
