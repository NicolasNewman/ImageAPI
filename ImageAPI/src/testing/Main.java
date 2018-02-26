package testing;

import java.util.Arrays;

import core.Color;
import core.ComplexImage;
import core.Corner;
import core.SimpleImage;

public class Main {
	
	public static void main(String[] args) {
		ComplexImage img = new ComplexImage("src/img/Sample.jpg");
		ComplexImage img2 = img.copy();
		img2.cannyEdge(1, 100, 50);
		img2.save("src/img/nms.jpg", "jpg");
		//System.out.println(Arrays.toString(Color.HSVtoRGB(-90, 1, 1)));
	}
}
