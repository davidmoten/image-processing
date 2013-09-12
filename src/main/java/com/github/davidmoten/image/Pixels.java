package com.github.davidmoten.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Pixels {

	private final int[][] pixels;

	public Pixels(BufferedImage img) {
		pixels = getPixels(img);
	}

	private static int[][] getPixels(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += ((pixels[pixel] & 0xff) << 24); // alpha
				argb += (pixels[pixel + 1] & 0xff); // blue
				argb += ((pixels[pixel + 2] & 0xff) << 8); // green
				argb += ((pixels[pixel + 3] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += (pixels[pixel] & 0xff); // blue
				argb += ((pixels[pixel + 1] & 0xff) << 8); // green
				argb += ((pixels[pixel + 2] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return result;
	}

	public int height() {
		return pixels[0].length;
	}

	public int width() {
		return pixels.length;
	}

	public int rgb(int x, int y) {
		return pixels[x][y];
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
		return (rgb >> 32) & 0x000000FF;
	}
}
