package com.github.davidmoten.image;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {

	private final Pixels pixels;

	public Pixels getPixels() {
		return pixels;
	}

	public Image(BufferedImage img) {
		pixels = new Pixels(img);
	}

	public static Image fromClasspath(String path) {
		try {
			return new Image(
					ImageIO.read(Image.class.getResourceAsStream(path)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void findBoundaries() {
		for (int x = 0; x < pixels.width(); x++)
			for (int y = 0; y < pixels.height(); y++)
				System.out.println(Pixels.red(pixels.rgb(x, y)) + " "
						+ Pixels.alpha(pixels.rgb(x, y)));
	}
}
