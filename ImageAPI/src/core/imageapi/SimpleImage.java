package core.imageapi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SimpleImage {
	
	protected BufferedImage img;
	protected int width;
	protected int height;
	protected String path;
	
	/**
	 * Creates a new SimpleImage from the given path
	 * @param path path of the original image
	 */
	public SimpleImage(String path) {
		this.path = path;
		try {
			img = ImageIO.read(new File(path));
			height = img.getHeight();
			width = img.getWidth();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new simple image with the given width and height
	 * @param w width of the image
	 * @param h height of the image
	 */
	public SimpleImage(int w, int h) {
		width = w;
		height = h;
		img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Copies a SimpleImage to another SimpleImage
	 * @return the copied data of a SimpleImage
	 */
	public SimpleImage copy() {
		SimpleImage img2 = new SimpleImage(width, height);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int[] rgb = getRGB(x, y);
				img2.setRGB(x, y, rgb[0], rgb[1], rgb[2]);
			}
		}
		return img2;
	}
	
	//TODO: single parameter
	public void save(String path, String extension) {
		File outputFile = new File(path);
		try {
			ImageIO.write(img, extension, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an array of the RGB value at the target pixel
	 * @param x x value of the pixel
	 * @param y y value of the pixel
	 * @return array containing the RGB values in the form [r,g,b]
	 */
	public int[] getRGB(int x, int y) {
		Color c = new Color(img.getRGB(x, y));
		return c.getRGBArray();
	}
	
	/**
	 * Returns an RGB value in the form of an int
	 * @param x x value of the pixel
	 * @param y y value of the pixel
	 * @return RGB value in the form of an int
	 */
	public int getRGBInt(int x, int y) {
		return img.getRGB(x, y);
	}
	
	/**
	 * Sets the color value at x,y to r,g,b
	 * @param x x value of the pixel
	 * @param y y value of the pixel
	 * @param r r value
	 * @param g g value
	 * @param b b value
	 */
	public void setRGB(int x, int y, int r, int g, int b) {
		Color c = new Color(r, g, b);
		img.setRGB(x, y, c.getRGB());
	}
	
	/**
	 * Sets the color value at x,y to a rgb value in the form of an int
	 * @param x x value of the pixel
	 * @param y y value of the pixel
	 * @param rgb rgb value in the form of an int
	 */
	public void setRGB(int x, int y, int rgb) {
		Color c = new Color(rgb);
		img.setRGB(x, y, c.getRGB());
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getA(int x, int y) { return (img.getRGB(x, y) >> 24) & 0xFF; }
	public int getR(int x, int y) { return (img.getRGB(x, y) >> 16) & 0xFF; }
	public int getG(int x, int y) { return (img.getRGB(x, y) >> 8) & 0xFF; }
	public int getB(int x, int y) { return img.getRGB(x, y) & 0xFF; }
	
	//public void setA(int x, int y, int a) { setRGB(x, y, getR(x, y), getG(x, y), getB(x, y), a); }
	public void setR(int x, int y, int r) { setRGB(x, y, r, getG(x, y), getB(x, y)); }
	public void setG(int x, int y, int g) { setRGB(x, y, getR(x, y), g, getB(x, y)); }
	public void setB(int x, int y, int b) { setRGB(x, y, getR(x, y), getG(x, y), b); }
}
