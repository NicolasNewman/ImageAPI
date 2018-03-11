package core.imageapi;

public class TestingImage extends ComplexImage {

	public TestingImage(String path) {
		super(path);
	}
	

	public TestingImage(int width, int height) {
		super(width, height);
	}


	public TestingImage copy() {
		TestingImage img2 = new TestingImage(width, height);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int[] rgb = getRGB(x, y);
				img2.setRGB(x, y, rgb[0], rgb[1], rgb[2]);
			}
		}
		return img2;
	}
	
	public void nonMaximumSupressionTesting(String name, int th1, int th2, int blur, boolean withColor) {
		boxBlur(blur);
		int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		int[][] gY = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
		grayscale();
		int maxG = -1;
		double[][] edge = new double[width][height];
		int[][] gArray = new int[width][height];
		double[][] tArray = new double[width][height];
		double[][] color = new double[width][height];
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
			    	color[w][h] = 180+theta;
			    } else {
			    	color[w][h] = theta;
			    }
			    theta = roundTheta(theta);
			    tArray[w][h] = theta;
			    
			    if(maxG < g) {
			    	maxG = (int) g;
			    }
			}
		}
	
 		double scale = 255.0 / maxG;
 		saveImageTesting(edge, scale, name + "_1-Sobel.jpg");
 		
 		if(withColor) {
 			saveImageColor(edge, color, scale, name + "_1--SobelColor.jpg");
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
 		
 		saveImageTesting(edge, scale, name + "_2-Thining.jpg");
 		
 		
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
 		
 		saveImageTesting(edge, scale, name + "_3-Threshold.jpg");
 		
 		double edge2[][] = copy(edge);
 		for(int w = 2; w < width-2; w++) {
 			for(int h = 2; h < height-2; h++) {
 				if(edge2[w][h] == maxG / 2) {
 					if(edge2[w-1][h-1] != maxG && edge2[w-1][h] != maxG && edge2[w-1][h+1] != maxG
 							&& edge2[w][h-1] != maxG && edge2[w][h+1] != maxG
 							&& edge2[w+1][h-1] != maxG && edge2[w+1][h] != maxG && edge2[w+1][h+1] != maxG) {
 						edge2[w][h] = 0;
 					} else {
 						//edge2[w][h] = 9;
 					}
				}
 			}
 		}
 		
 		saveImageTesting(edge2, scale, name + "_5-Final.jpg");
 		
 		for(int w = 2; w < width-2; w++) {
 			for(int h = 2; h < height-2; h++) {
 				if(edge[w][h] == maxG / 2) {
 					if(edge[w-1][h-1] != maxG && edge[w-1][h] != maxG && edge[w-1][h+1] != maxG
 							&& edge[w][h-1] != maxG && edge[w][h+1] != maxG
 							&& edge[w+1][h-1] != maxG && edge[w+1][h] != maxG && edge[w+1][h+1] != maxG) {
 						edge[w][h] = 8; //  red
 					} else {
 						edge[w][h] = 7; // blue
 					}
				}
 			}
 		}
 		
 		saveImageTestingBlob(edge, scale, name + "_4-Blob.jpg");
	
		
	}
	
	public double[][] copy(double[][] src) {
		double[][] out = new double[src.length][src[0].length];
		for(int i = 0; i < src.length; i++) {
			for(int j = 0; j < src[0].length; j++) {
				out[i][j] = src[i][j];
			}
		}
		return out;
	}
	
	public void saveImageTesting(double[][] edge, double scale, String filename) {
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				int edgeColor = (int) edge[w][h];
				edgeColor = (int)(edgeColor*scale);
				edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;
				setRGB(w, h, edgeColor);
			}
		}
		save("src/img/canny/" + filename, "jpg");
	}
	
	public void saveImageColor(double[][] edge, double[][] color, double scale, String filename) {
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				double edgeColor = edge[w][h];
				edgeColor = (edgeColor*scale);
				int[] rgb = Color.HSVtoRGB(color[w][h], edgeColor/255, edgeColor/255);
				setRGB(w, h, rgb[0], rgb[1], rgb[2]);
			}
		}
		save("src/img/canny/" + filename, "jpg");
	}
	
	public void saveImageTestingBlob(double[][] edge, double scale, String filename) {
		for(int w = 1; w < width-1; w++) {
			for(int h = 1; h < height-1; h++) {
				if(edge[w][h] == 8) {
					setRGB(w, h, 255, 0, 0);
				} else if(edge[w][h] == 7) {
					setRGB(w, h, 0, 0, 255);
				} else {
					int edgeColor = (int) edge[w][h];
					edgeColor = (int)(edgeColor*scale);
					edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;
					setRGB(w, h, edgeColor);
				}
			}
		}
		save("src/img/canny/" + filename, "jpg");
	}

}
