package core.imageapi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class ComplexImage extends SimpleImage {

	public ComplexImage(String path) {
		super(path);
	}
	
	public ComplexImage(int w, int h) {
		super(w, h);
	}
	
	public ComplexImage copy() {
		ComplexImage img2 = new ComplexImage(width, height);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int[] rgb = getRGB(x, y);
				img2.setRGB(x, y, rgb[0], rgb[1], rgb[2]);
			}
		}
		return img2;
	}
	
	/**
	 * Grayscales an image
	 * This is the average value for the three channels
	 */
	public void grayscale() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int avg = (getR(x, y) + getG(x, y) + getB(x, y)) / 3;
				setRGB(x, y, avg, avg, avg);
			}
		}
	}
	
	/**
	 * Grayscales an image
	 * @param img image to grayscale
	 * @return img grayscaled
	 */
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
	
	/**
	 * Negates an image
	 * This is (255-value) for each channel
	 */
	public void negate() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				setR(x, y, 255 - getR(x, y));
				setG(x, y, 255 - getG(x, y));
				setB(x, y, 255 - getB(x, y));
			}
		}
	}
	
	/**
	 * Negates an image
	 * @param img image to negate
	 * @return img negated
	 */
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
	
	/**
	 * Mirrors an image over its vertical axis
	 * @param leftToRight half of the image to mirror over
	 */
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
	
	/**
	 * Mirrors an image over its horizontal axis
	 * @param topToBottom half of the image to mirror over
	 */
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
	
	/**
	 * Mirrors an image diagonally. If w != h, only a square region will be mirrored
	 * @param c the corner to place the diagonal
	 * @param bottomToTop section of the image to mirror over
	 */
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
	
	/**
	 * Blurs an image. This is slower but more accurate then box blur
	 * @param k kernal size (1 = 3x3, 2 = 5x5, etc)
	 */
	public void gaussianBlur(int k) {
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				int rAvg = 0, gAvg = 0, bAvg = 0;
				int length = (k*2+1)*(k*2+1);
				for(int kw = 0; kw < k*2+1; kw++) {
					for(int kh = 0; kh < k*2+1; kh++) {
						int[] loc = {w-(k-kw), h-(k-kh)};
						if(loc[0] > 0 && loc[0] < width) {
							if(loc[1] > 0 && loc[1] < height) {
								rAvg += getR(loc[0], loc[1]);
								gAvg += getG(loc[0], loc[1]);
								bAvg += getB(loc[0], loc[1]);
							}
						}
					}
				}
				rAvg /= length;
				gAvg /= length;
				bAvg /= length;
				setRGB(w, h, rAvg, gAvg, bAvg);
			}
		}
	}
	
	/**
	 * Blurs an image. This is faster but less accurate then guassian blur
	 * @param k kernal size (1 = 3x3, 2 = 5x5, etc)
	 */
	public void boxBlur(int k) {
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				int rAvg = 0, gAvg = 0, bAvg = 0;
				int length = (k*2+1);
				for(int kw = 0; kw < k*2+1; kw++) {
					int[] loc = {w-(k-kw), h};
					if(loc[0] > 0 && loc[0] < width) {
						if(loc[1] > 0 && loc[1] < height) {
							rAvg += getR(loc[0], loc[1]);
							gAvg += getG(loc[0], loc[1]);
							bAvg += getB(loc[0], loc[1]);
						}
					}
				}
				rAvg /= length;
				gAvg /= length;
				bAvg /= length;
				setRGB(w, h, rAvg, gAvg, bAvg);
			}
		}
		
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				int rAvg = 0, gAvg = 0, bAvg = 0;
				int length = (k*2+1);
				for(int kh = 0; kh < k*2+1; kh++) {
					int[] loc = {w, h-(k-kh)};
					if(loc[0] > 0 && loc[0] < width) {
						if(loc[1] > 0 && loc[1] < height) {
							rAvg += getR(loc[0], loc[1]);
							gAvg += getG(loc[0], loc[1]);
							bAvg += getB(loc[0], loc[1]);
						}
					}
				}
				rAvg /= length;
				gAvg /= length;
				bAvg /= length;
				setRGB(w, h, rAvg, gAvg, bAvg);
			}
		}
	}
	
	/**
	 * Applies canny edge detection to an image.
	 * @param k kernal size (1 = 3x3, 2 = 5x5, etc)
	 * @param th1 upper bounds for the threshold
	 * @param th2 lower bound for the threshold
	 */
	public void cannyEdge(int k, int th1, int th2) {
		boxBlur(k);
		nonMaximumSupression(th1, th2);
	}
	
	/**
	 * Applies a sobel operator to an image. This is a less sophisticated version of canny edge
	 */
	public void sobelOperator() {
		int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		int[][] gY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
		grayscale();
		int maxG = -1;
		int[][] edge = new int[width][height];
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				int pixel_x = (gX[0][0] * getR(w-1,h-1)) + (gX[0][2] * getR(w-1,h+1)) +
			              (gX[1][0] * getR(w,h-1)) + (gX[1][2] * getR(w,h+1)) +
			              (gX[2][0] * getR(w+1,h-1)) + (gX[2][2] * getR(w+1,h+1));
			 
			    int pixel_y = (gY[0][0] * getR(w-1,h-1)) + (gY[0][1] * getR(w-1,h)) + (gY[0][2] * getR(w-1,h+1)) +
			              (gY[2][0] * getR(w+1,h-1)) + (gY[2][1] * getR(w+1,h)) + (gY[2][2] * getR(w+1,h+1));
			    int g = Math.abs(pixel_x) + Math.abs(pixel_y);
			    if(maxG < g) {
			    	maxG = g;
			    }
			    edge[w][h] = g;
			}
		}
		
		double scale = 255.0 / maxG;
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				int edgeColor = edge[w][h];
				edgeColor = (int)(edgeColor*scale);
				edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;
				setRGB(w, h, edgeColor);
			}
		}
	}
	
	protected double roundTheta(double t) {
		if(t < 22.5 || t >= 157.5) {
			t = 0;
		} else if(t >= 22.5 && t < 67.5) {
			t = 45;
		} else if(t >= 67.5 && t < 112.5) {
			t = 90;
		} else if(t >= 112.5 && t < 157.5) {
			t = 135;
		}
		return t;	
	}
	
	/**
	 * Applies the remain steps for canny edge after the first
	 * @param th1
	 * @param th2
	 */
	protected void nonMaximumSupression(int th1, int th2) {
		int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		int[][] gY = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
		grayscale();
		int maxG = -1;
		double[][] edge = new double[width][height];
		double[][] tArray = new double[width][height];
		
		// Non-max suppression
 		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
			    double pixel_x = (gX[0][0] * getR(w-1,h-1)) + (gX[0][2] * getR(w-1,h+1)) +
			              (gX[1][0] * getR(w,h-1)) + (gX[1][2] * getR(w,h+1)) +
			              (gX[2][0] * getR(w+1,h-1)) + (gX[2][2] * getR(w+1,h+1));
			 
			    double pixel_y = (gY[0][0] * getR(w-1,h-1)) + (gY[0][1] * getR(w-1,h)) + (gY[0][2] * getR(w-1,h+1)) +
			              (gY[2][0] * getR(w+1,h-1)) + (gY[2][1] * getR(w+1,h)) + (gY[2][2] * getR(w+1,h+1));
			    
			    double g = Math.hypot(pixel_x, pixel_y);
			    edge[w][h] = g;
			    
			    double theta = Math.atan2(pixel_y, pixel_x)*180/Math.PI;
			    if(theta < 0) {
			    	theta += 180;
			    }
			    theta = roundTheta(theta);
			    tArray[w][h] = theta;
			    
			    if(maxG < g) {
			    	maxG = (int) g;
			    }
			}
		}
 		double scale = 255.0 / maxG;
 		
 		// Edge thining
 		for(int w = 1; w < width-1; w++) {
 			for(int h = 1; h < height-1; h++) {
 				if(tArray[w][h] == 0) { // 0
			    	if(edge[w][h] <= edge[w][h-1] || edge[w][h] <= edge[w][h+1]) {
			    		edge[w][h] = 0;
			    	}
			    } else if(tArray[w][h] == 45) { // 45
			    	if(edge[w][h] <= edge[w-1][h+1] || edge[w][h] <= edge[w+1][h-1]) {
			    		edge[w][h] = 0;
			    	}
			    } else if(tArray[w][h] == 90) { // 90
			    	if(edge[w][h] <= edge[w-1][h] || edge[w][h] <= edge[w+1][h]) {
			    		edge[w][h] = 0;
			    	}
			    } else if(tArray[w][h] == 135) { // 135
			    	if(edge[w][h] <= edge[w-1][h-1] || edge[w][h] <= edge[w+1][h+1]) {
			    		edge[w][h] = 0;
			    	}
			    }
 			}
 		}
 		
 		// Tresholding
 		for(int w = 1; w < width-1; w++) {
 			for(int h = 1; h < height-1; h++) {
 				if(edge[w][h] < th2) {
 					edge[w][h] = 0;
 				} else if(edge[w][h] < th1 && edge[w][h] > th2) {
 					edge[w][h] = maxG / 2;
 				} else if(edge[w][h] > th1) {
 					edge[w][h] = maxG;
 				}
 			}
 		}
 		
 		// Blob operator
 		for(int w = 2; w < width-2; w++) {
 			for(int h = 2; h < height-2; h++) {
 				if(edge[w][h] == maxG / 2) {
 					if(edge[w-1][h-1] != maxG && edge[w-1][h] != maxG && edge[w-1][h+1] != maxG
 							&& edge[w][h-1] != maxG && edge[w][h+1] != maxG
 							&& edge[w+1][h-1] != maxG && edge[w+1][h] != maxG && edge[w+1][h+1] != maxG) {
 						edge[w][h] = 0;
 					}
				}
 			}
 		}
	
 		// Scaling
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				int edgeColor = (int) edge[w][h];
				edgeColor = (int)(edgeColor*scale);
				edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;
				setRGB(w, h, edgeColor);
			}
		}
	}
	
	/**
	 * Applies a sobel operator to an image that includes edge color based on its angle. This is a less sophisticated version of canny edge
	 */
	public void sobelOperatorColor() {
		int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		int[][] gY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
		grayscale();
		int maxG = -1;
		int[][] edge = new int[width][height];
		double[][] color = new double[width][height];
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				int pixel_x = (gX[0][0] * getR(w-1,h-1)) + (gX[0][2] * getR(w-1,h+1)) +
			              (gX[1][0] * getR(w,h-1)) + (gX[1][2] * getR(w,h+1)) +
			              (gX[2][0] * getR(w+1,h-1)) + (gX[2][2] * getR(w+1,h+1));
			 
			    int pixel_y = (gY[0][0] * getR(w-1,h-1)) + (gY[0][1] * getR(w-1,h)) + (gY[0][2] * getR(w-1,h+1)) +
			              (gY[2][0] * getR(w+1,h-1)) + (gY[2][1] * getR(w+1,h)) + (gY[2][2] * getR(w+1,h+1));
			    int g = (int) Math.hypot(pixel_x, pixel_y);
			    if(maxG < g) {
			    	maxG = g;
			    }
			    edge[w][h] = g;
			    
			    /*
			    //TODO: Change?
			    if(pixel_x != 0) {
			    	color[w][h] = (int) Math.floor((Math.atan2(pixel_y, pixel_x)*180/Math.PI));
			    	if(color[w][h] < 0) {
			    		color[w][h] = 360 - Math.abs(color[w][h]);
			    	}
			    } else {
			    	color[w][h] = 0;
			    }*/
			    
			    double theta = Math.atan2(pixel_y, pixel_x)*180/Math.PI;
			    if(theta < 0) {
			    	theta += 360;
			    	color[w][h] = theta;
			    } else {
			    	color[w][h] = theta;
			    }
			}
		}
		
		double scale = 255.0 / maxG;
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				double edgeColor = edge[w][h];
				edgeColor = (edgeColor*scale);
				int[] rgb = Color.HSVtoRGB(color[w][h], edgeColor/255, edgeColor/255);
				setRGB(w, h, rgb[0], rgb[1], rgb[2]);
			}
		}
	}
}