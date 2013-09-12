package com.github.davidmoten.image;

import java.awt.image.BufferedImage;

public class Pixels {

	private final BufferedImage img;

	public Pixels(BufferedImage img) {
		this.img = img;
	}

	public int height() {
		return img.getHeight();
	}

	public int width() {
		return img.getWidth();
	}

	public int rgb(int x, int y) {
		return img.getRGB(x, y);
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
