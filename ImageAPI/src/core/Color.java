package core;

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
