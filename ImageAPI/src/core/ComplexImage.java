package core;

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
	
	public void cannyEdge(int k, int th1, int th2) {
		boxBlur(k);
		nonMaximumSupression(th1, th2);
	}
	
	// https://stackoverflow.com/questions/41468661/sobel-edge-detecting-program-in-java
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
	
	private double roundTheta(double t) {
		//0 to 45 (22.5) t < 22.5: 0
		//45 to 90 (67.5) t < 67.5: 45
		//90 to 135 (112.5) t < 112.5: 90
		//135 to 0 (157.5) t < 157.5: 135
		
		// t < 22.5 or t >= 157.5 : 0
		// (t >= 22.5 and t <) or t < 67.5 : 45
		// t >= 67.5 or t < 112.5 : 90
		// t >= 112.5 or t < 157.5: 135
		
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
	
	public void nonMaximumSupression(int th1, int th2) {
		int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		int[][] gY = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
		grayscale();
		int maxG = -1;
		double[][] edge = new double[width][height];
		int[][] gArray = new int[width][height];
		double[][] tArray = new double[width][height];
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
			    //double theta = Math.atan(pixel_y / pixel_x)*180/Math.PI;
//			    if(theta < 0) {
//			    	//theta += 360;
//			    	//theta = Math.abs(theta);
//			    	//System.out.println(theta);
//			    	//theta += 180;
//			    	theta += 180;
//			    	//System.out.println(theta);
//			    }
			    //System.out.println("Before: " + theta);
			    theta = Math.abs(theta % 180);
			    //System.out.println("After: " + theta);
			    theta = roundTheta(theta);
			    tArray[w][h] = theta;
			    
			    if(maxG < g) {
			    	maxG = (int) g;
			    }
			}
		}
 		
 		for(int w = 1; w < width-1; w++) {
 			for(int h = 1; h < height-1; h++) {
 				if(tArray[w][h] == 0) { // 0
			    	if(edge[w][h] >= edge[w][h-1] && edge[w][h] >= edge[w][h+1]) {
			    		//edge[w][h] = edge[w][h];
			    	} else {
			    		edge[w][h] = 0;
			    	}
			    } else if(tArray[w][h] == 45) { // 45
			    	if(edge[w][h] >= edge[w-1][h+1] && edge[w][h] >= edge[w+1][h-1]) {
			    		//edge[w][h] = edge[w][h];
			    	} else {
			    		edge[w][h] = 0;
			    	}
			    } else if(tArray[w][h] == 90) { // 90
			    	if(edge[w][h] >= edge[w-1][h] && edge[w][h] >= edge[w+1][h]) {
			    		//edge[w][h] = edge[w][h];
			    	} else {
			    		edge[w][h] = 0;
			    	}
			    } else if(tArray[w][h] == 135) { // 135
			    	if(edge[w][h] >= edge[w-1][h-1] && edge[w][h] >= edge[w+1][h+1]) {
			    		//edge[w][h] = edge[w][h];
			    	} else {
			    		edge[w][h] = 0;
			    	}
			    }
 			}
 		}
	
		double scale = 255.0 / maxG;
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				int edgeColor = (int) edge[w][h];
				edgeColor = (int)(edgeColor*scale);
				edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;
				setRGB(w, h, edgeColor);
			}
		}
	}
	
	public void sobelOperatorColor() {
		int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		int[][] gY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
		grayscale();
		int maxG = -1;
		int[][] edge = new int[width][height];
		int[][] color = new int[width][height];
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
			    
			    if(pixel_x != 0) {
			    	color[w][h] = (int) Math.floor((Math.atan2(pixel_y, pixel_x)*180/Math.PI));
			    	if(color[w][h] < 0) {
			    		color[w][h] = 360 - Math.abs(color[w][h]);
			    	}
			    } else {
			    	color[w][h] = 0;
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


/*
public void nonMaximumSupression(int th1, int th2) {
	int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
	int[][] gY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
	grayscale();
	int maxG = -1;
	int[][] edge = new int[width][height];
	int[][] gArray = new int[width][height];
	double[][] tArray = new double[width][height];
		for(int w = 1; w < width-1; w++) {
		for(int h = 1; h < height-1; h++) {
			double pixel_x = (gX[0][0] * getR(w-1,h-1)) + (gX[0][2] * getR(w-1,h+1)) +
		              (gX[1][0] * getR(w,h-1)) + (gX[1][2] * getR(w,h+1)) +
		              (gX[2][0] * getR(w+1,h-1)) + (gX[2][2] * getR(w+1,h+1));
		 
		    double pixel_y = (gY[0][0] * getR(w-1,h-1)) + (gY[0][1] * getR(w-1,h)) + (gY[0][2] * getR(w-1,h+1)) +
		              (gY[2][0] * getR(w+1,h-1)) + (gY[2][1] * getR(w+1,h)) + (gY[2][2] * getR(w+1,h+1));
		    
		    int g = (int) Math.hypot(pixel_x, pixel_y);
		    edge[w][h] = g;
		    
		    //double theta = Math.atan2(pixel_y, pixel_x)*180/Math.PI;
		    //double theta = Math.atan(pixel_y / pixel_x)*180/Math.PI;
//		    if(theta < 0) {
//		    	//theta += 360;
//		    	//theta = Math.abs(theta);
//		    	//System.out.println(theta);
//		    	//theta += 180;
//		    	theta += 180;
//		    	//System.out.println(theta);
//		    }
		    theta = Math.abs(theta % 180);
		    theta = roundTheta(theta);
		    tArray[w][h] = theta;
		    
		    if(maxG < g) {
		    	maxG = g;
		    }
		}
	}
		
//		for(int w = 1; w < width-1; w++) {
//			for(int h = 1; h < height-1; h++) {
//				if(tArray[w][h] == 0) {
//		    	if(edge[w][h] >= edge[w+1][h] && edge[w][h] >= edge[w-1][h]) {
//		    		//edge[w][h] = edge[w][h];
//		    	} else {
//		    		edge[w][h] = 0;
//		    	}
//		    } else if(tArray[w][h] == 45) {
//		    	if(edge[w][h] >= edge[w+1][h+1] && edge[w][h] >= edge[w-1][h-1]) {
//		    		//edge[w][h] = edge[w][h];
//		    	} else {
//		    		edge[w][h] = 0;
//		    	}
//		    } else if(tArray[w][h] == 90) {
//		    	if(edge[w][h] >= edge[w][h+1] && edge[w][h] >= edge[w][h-1]) {
//		    		//edge[w][h] = edge[w][h];
//		    	} else {
//		    		edge[w][h] = 0;
//		    	}
//		    } else if(tArray[w][h] == 135) {
//		    	if(edge[w][h] >= edge[w-1][h+1] && edge[w][h] >= edge[w+1][h-1]) {
//		    		//edge[w][h] = edge[w][h];
//		    	} else {
//		    		edge[w][h] = 0;
//		    	}
//		    }
//			}
//		}
*/