package util;

import java.awt.image.BufferedImage;


public class ImageUtil {

	/**
	 * resize the image to fixed size
	 * @param originalImage the original image
	 * @param type type of image
	 * @param width the output width
	 * @param height the output height
	 * @return resized image
	 */
	public static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height){
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		java.awt.Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
	 
		return resizedImage;
	    }
}
