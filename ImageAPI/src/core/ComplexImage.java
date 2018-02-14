package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ComplexImage extends SimpleImage {

	public ComplexImage(String path) {
		super(path);
	}
	
	public ComplexImage(int w, int h) {
		super(w, h);
	}
	
	public ComplexImage(int w, int h, BufferedImage source) {
		super(w, h, source);
	}
	
	public ComplexImage copy() {
		ComplexImage img2 = new ComplexImage(width, height, img);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int[] rgb = getRGB(x, y);
				img2.setRGB(x, y, rgb[0], rgb[1], rgb[2]);
			}
		}
		return img2;
	}
	
	public void grayscale() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int avg = (getR(x, y) + getG(x, y) + getB(x, y)) / 3;
				setRGB(x, y, avg, avg, avg);
			}
		}
	}
	
	public static ComplexImage grayscale(ComplexImage img) {
		ComplexImage temp = img.copy();
		for(int x = 0; x < temp.width; x++) {
			for(int y = 0; y < temp.height; y++) {
				int avg = (temp.getR(x, y) + temp.getG(x, y) + temp.getB(x, y)) / 3;
				temp.setRGB(x, y, avg, avg, avg);
			}
		}
		return temp;
	}
	
	public void negate() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				setR(x, y, 255 - getR(x, y));
				setG(x, y, 255 - getG(x, y));
				setB(x, y, 255 - getB(x, y));
			}
		}
	}
	
	public static ComplexImage negate(ComplexImage img) {
		ComplexImage temp = img.copy();
		for(int x = 0; x < temp.width; x++) {
			for(int y = 0; y < temp.height; y++) {
				temp.setR(x, y, 255 - temp.getR(x, y));
				temp.setG(x, y, 255 - temp.getG(x, y));
				temp.setB(x, y, 255 - temp.getB(x, y));
			}
		}
		return temp;
	}
	
	public void mirrorVertically(boolean leftToRight) {
		if(leftToRight) {
			for(int row = 0; row < height; row++) {
				for(int col = 0; col < width / 2; col++) {
					int[] leftColor = getRGB(col, row);
					setRGB(width-1-col, row, leftColor[0], leftColor[1], leftColor[2]);
				}
			}
		} else {
			for(int row = 0; row < height; row++) {
				for(int col = 0; col < width / 2; col++) {
					int[] leftColor = getRGB(width-1-col, row);
					setRGB(col, row, leftColor[0], leftColor[1], leftColor[2]);
				}
			}
		}
	}
	
	public void mirrorHorizontally(boolean topToBottom) {
		if(topToBottom) {
			for(int row = 0; row < height / 2; row++) {
				for(int col = 0; col < width; col++) {
					int[] leftColor = getRGB(col, row);
					setRGB(col, height-1-row, leftColor[0], leftColor[1], leftColor[2]);
				}
			}
		} else {
			for(int row = 0; row < height / 2; row++) {
				for(int col = 0; col < width; col++) {
					int[] leftColor = getRGB(col, height-1-row);
					setRGB(col, row, leftColor[0], leftColor[1], leftColor[2]);
				}
			}
		}
	}
	
	public void mirrorDiagonally(Corner c, boolean bottomToTop) {
		int w2 = 0;
		int h2 = 0;
		int[][] region = null;
		if(c == Corner.TL || c == Corner.BL) {
			if (width >= height) {
				w2 = height;
				region = new int[][] {{0, 0}, {w2, height}};
			} else {
				h2 = width;
				region = new int[][] {{0, 0}, {width, h2}};
			}
		} else if (c == Corner.TR || c == Corner.BR) {
			if (width >= height) {
				w2 = height;
				region = new int[][] {{w2, 0}, {width, height}};
			} else {
				h2 = width;
				region = new int[][] {{0, h2}, {width, height}};
			}
		}
		
		if(c == Corner.TL || c == Corner.BR) {
			int offset = region[0][0];
			for(int h = region[0][1]; h < region[1][1]; h++) {
				for(int w = region[0][0]; w < region[1][0]; w++) {
					//if(c == Corner.TL || c == Corner.BR) {
						if(bottomToTop) {
							if(w < offset) {
								int[] rgb = getRGB(w, h);
								if(c == Corner.TL) { // TL bottom to top
									setRGB(h, w, rgb[0], rgb[1], rgb[2]);
								} else if(c == Corner.BR) { // BR bottom to top
									int temp = offset - w;
									setRGB(w+temp, h-temp, rgb[0], rgb[1], rgb[2]);
								}
							}
						} else {
							if(w > offset) {
								int[] rgb = getRGB(w, h);
								if(c == Corner.TL) { // TL top to bottom
									setRGB(h, w, rgb[0], rgb[1], rgb[2]);
								} else if(c == Corner.BR) {
									int temp = offset-w; // BR top to bottom
									setRGB(w+temp, h-temp, rgb[0], rgb[1], rgb[2]);
								}
							}
						}
					//}
				}
				offset++;
			}
		} else {
			int offset = region[1][0];
			for(int h = region[0][1]; h < region[1][1]; h++) {
				for(int w = region[0][0]; w < region[1][0]; w++) {
					if(bottomToTop) {
						if (w > offset) {
							int[] rgb = getRGB(w, h);
							if(c == Corner.BL) { // BL bottom to top
								int temp = w-offset;
								setRGB(w-temp-1, h-temp-1, rgb[0], rgb[1], rgb[2]);
							} else if(c == Corner.TR) { // TR bottom to top
								int temp = w-offset;
								setRGB(w-temp-1, h-temp-1, rgb[0], rgb[1], rgb[2]);
							}
						}
					} else {
						if(w < offset) {
							int[] rgb = getRGB(w, h);
							if(c == Corner.BL) { // BL top to bottom
								int temp = offset-w;
								setRGB(w+temp-1, h+temp-1, rgb[0], rgb[1], rgb[2]);
							} else if(c == Corner.TR) { // TR top to bottom
								int temp = offset-w;
								setRGB(w+temp-1, h+temp-1, rgb[0], rgb[1], rgb[2]);
							}
						}
					}
				}
				offset--;
			}
		}
	}
}
