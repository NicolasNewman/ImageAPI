package testing;

import core.ComplexImage;
import core.Corner;
import core.SimpleImage;

public class Main {
	
	public static void main(String[] args) {
		ComplexImage img = new ComplexImage("src/img/island.jpg");
		ComplexImage img2 = img.copy();
		img2.mirrorDiagonally(Corner.TL, false);
		img2.mirrorDiagonally(Corner.TR, false);
		img2.mirrorVertically(true);
		img2.mirrorHorizontally(false);
		img2.save("src/img/islandEdit.jpg", "jpg");
		
		img = new ComplexImage("src/img/triforce.jpg");
		img2 = img.copy();
		img2.mirrorDiagonally(Corner.TL, false);
		img2.mirrorVertically(true);
		img2.mirrorHorizontally(true);
		img2.save("src/img/triforceEdit.jpg", "jpg");
		
		img = new ComplexImage("src/img/overwatch.png");
		img2 = img.copy();
		img2.mirrorDiagonally(Corner.TL, false);
		img2.mirrorVertically(true);
		img2.mirrorHorizontally(true);
		img2.save("src/img/overwatchEdit.png", "png");
	}

}
