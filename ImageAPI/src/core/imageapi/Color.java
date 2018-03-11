package core.imageapi;

public class Color {
	
	private int rgb;
	
	public Color(int r, int g, int b) {
		rgb = (r << 16 | g << 8 | b);
	}
	
	public Color(int r, int g, int b, int a) {
		rgb = (a << 24 | r << 16 | g << 8 | b << 0);
	}
	
	public Color(int rgb) {
		this.rgb = rgb;
	}
	
	public static int[] HSVtoRGB(double h, double s, double v) {
		double c = v * s;
		double x = c * (1 - Math.abs(((h/60) % 2)-1));
		double m = v - c;
		double r = 0, g = 0, b = 0;
		
		if (h >= 0 && h < 60) {
			r = c;
			g = x;
			b = 0;
		} else if (h >= 60 && h < 120) {
			r = x;
			g = c;
			b = 0;
		} else if (h >= 120 && h < 180) {
			r = 0;
			g = c;
			b = x;
		} else if (h >= 180 && h < 240) {
			r = 0;
			g = x;
			b = c;
		} else if (h >= 240 && h < 300) {
			r = x;
			g = 0;
			b = c;
		} else if (h >= 300 && h < 360) {
			r = c;
			g = 0;
			b = x;
		}
		
		r = (r+m)*255;
		g = (g+m)*255;
		b = (b+m)*255;
		
		return new int[] {(int) r, (int) g, (int) b};
		
	}
	
	public int getRGB() {
		return rgb;
	}
	
	public int[] getRGBArray() {
		return new int[] {getRed(), getGreen(), getBlue()};
	}
	
	public int getRed() {
		return (getRGB() >> 16) & 0x000000FF;
	}
	
	public int getGreen() {
		return (getRGB() >> 8) & 0x000000FF;
	}
	
	public int getBlue() {
		return getRGB() & 0x000000FF;
	}

}
