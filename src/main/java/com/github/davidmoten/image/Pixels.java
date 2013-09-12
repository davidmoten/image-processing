package com.github.davidmoten.image;

import java.awt.image.BufferedImage;

public class Pixels {

	private final BufferedImage image;

	public Pixels(BufferedImage image) {
		this.image = image;
	}

	public int height() {
		return image.getHeight();
	}

	public int width() {
		return image.getWidth();
	}

	public int rgb(int x, int y) {
		return image.getRGB(x, y);
	}

	public void setRGB(int x, int y, int rgb) {
		image.setRGB(x, y, rgb);
	}

	public static int red(int rgb) {
		return (rgb >> 16) & 0x000000FF;
	}

	public static int green(int rgb) {
		return (rgb >> 8) & 0x000000FF;
	}

	public static int blue(int rgb) {
		return (rgb) & 0x000000FF;
	}

	public static int alpha(int rgb) {
		return (rgb >> 24) & 0x000000FF;
	}

}
