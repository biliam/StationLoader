package net.modificationstation.stationloader.common.api.dirtsword;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
public class TextureUV {
	
	
	private HashMap textureMap;
	
	public TextureUV() {
		textureMap = new HashMap();
	}
	public void applyTexture(InputStream inputStream) throws IOException {
		//opengl wizardry here at a later date
		BufferedImage imageToLoad = readTextureImage(inputStream);
		
		
	}
	private BufferedImage readTextureImage(InputStream inputstream) throws IOException{
		BufferedImage bufferedimage = ImageIO.read(inputstream);
		inputstream.close();
		return bufferedimage;
	 }
}
