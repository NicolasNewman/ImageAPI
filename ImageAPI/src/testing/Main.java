package testing;

import java.util.Arrays;

import core.imageapi.Color;
import core.imageapi.ComplexImage;
import core.imageapi.Corner;
import core.imageapi.SimpleImage;
import core.imageapi.TestingImage;
import core.math.Matrix;

public class Main {
	
	public static void main(String[] args) {
		/*TestingImage adt = new TestingImage("src/img/canny/adt.jpg");
		TestingImage sample = new TestingImage("src/img/canny/sample.jpg");
		TestingImage ship = new TestingImage("src/img/canny/ship.jpg");
		TestingImage sobel = new TestingImage("src/img/canny/sobel.jpg");
		TestingImage turtle = new TestingImage("src/img/canny/turtle.jpg");
		
		TestingImage adtCopy = adt.copy();
		TestingImage sampleCopy = sample.copy();
		TestingImage shipCopy = ship.copy();
		TestingImage sobelCopy = sobel.copy();
		TestingImage turtleCopy = turtle.copy();*/
		
		//adtCopy.nonMaximumSupressionTesting("adt", 90, 60, 1, true);
		//shipCopy.nonMaximumSupressionTesting("ship", 110, 90, 1, true);
		//sobelCopy.nonMaximumSupressionTesting("sobel", 50, 40, 1, true);
		//sampleCopy.nonMaximumSupressionTesting("sample", 250, 200, 1, true);
		//turtleCopy.nonMaximumSupressionTesting("turtle", 30, 20, 3, true);
		
		/*ComplexImage img = new ComplexImage("src/img/adt.jpg");
		ComplexImage imgCopy = img.copy();
		//imgCopy.cannyEdge(1, 90, 60);
		imgCopy.sobelOperatorColor();
		imgCopy.save("src/img/adtCopy.jpg", "jpg");*/
		
		ComplexImage img = new ComplexImage("src/img/nvidia.jpg");
		ComplexImage other = img.copy();
		
		
		/*other.boxBlur(4);
		other.mirrorHorizontally(true);
		other.mirrorVertically(true);
		other.mirrorDiagonally(Corner.TL, false);
		other.mirrorVertically(true);
		other.mirrorHorizontally(false);
		other.mirrorDiagonally(Corner.TL, false);
		other.mirrorVertically(true);
		other.mirrorHorizontally(false);
		
		other.mirrorDiagonally(Corner.TL, false);
		other.mirrorVertically(true);
		other.sobelOperatorColor();*/
		
		other.mirrorVertically(true);
		other.mirrorHorizontally(false);
		other.mirrorDiagonally(Corner.TL, true);
		// Break
		other.mirrorVertically(true);
		other.mirrorHorizontally(false);

		other.sobelOperatorColor();
		other.save("src/img/nvidia4.jpg", "jpg");
		
		

	}
}
