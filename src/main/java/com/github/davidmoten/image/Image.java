package com.github.davidmoten.image;

import ij.process.ColorProcessor;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Image {

	private final BufferedImage image;

	public Image(BufferedImage image) {
		this.image = image;
	}

	public static Image fromClasspath(String path) {
		try {
			return new Image(
					ImageIO.read(Image.class.getResourceAsStream(path)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final boolean processLine = true;

	public void findBoundaries(Line line, int maxDistanceToLine) {
		display(image);

		BufferedImage subImage;
		if (processLine) {
			emphasizeAroundLine(image, line, maxDistanceToLine);
			subImage = getSubImage(image, line, maxDistanceToLine);
		} else
			subImage = image;

		// use ImageJ
		ColorProcessor ip = new ColorProcessor(subImage);
		ip.findEdges();
		BufferedImage edges = ip.getBufferedImage();
		display(edges);
	}

	private static BufferedImage getSubImage(BufferedImage image, Line line,
			int maxDistanceToLine) {
		BufferedImage subImage;
		int minX = Math.min(line.x1(), line.x2());
		int maxX = Math.max(line.x1(), line.x2());
		int minY = Math.min(line.y1(), line.y2());
		int maxY = Math.max(line.y1(), line.y2());
		subImage = image.getSubimage(Math.max(0, minX - maxDistanceToLine),
				Math.max(0, minY - maxDistanceToLine), maxX - minX
						+ maxDistanceToLine, maxY - minY + maxDistanceToLine);
		return subImage;
	}

	private static void emphasizeAroundLine(BufferedImage image, Line line,
			int maxDistanceToLine) {
		log("emphasizing lines for image of " + image.getWidth()
				* image.getHeight() + " pixels");
		Line2D segment = new Line2D.Double(new Point2D.Double(line.x1(),
				line.y1()), new Point2D.Double(line.x2(), line.y2()));
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				int red = red(rgb);
				int green = green(rgb);
				int blue = blue(rgb);

				double distance = segment.ptSegDist(new Point2D.Double(x, y));
				double factor = Math.max(0, maxDistanceToLine - distance)
						/ maxDistanceToLine;

				float[] hsb = new float[3];
				Color.RGBtoHSB(red, green, blue, hsb);
				int color = Color.HSBtoRGB(hsb[0], hsb[1], (float) (hsb[2]
						* factor * factor));

				image.setRGB(x, y, color);
			}
	}

	public static void display(final BufferedImage image) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setSize(image.getWidth(), image.getHeight());
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					ImagePanel panel = new ImagePanel(image);
					frame.getContentPane().add(panel);
					// frame.pack();
					frame.setVisible(true);
					frame.repaint();
					log("frame visible");
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void log(String message) {
		System.out.println(message);
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

	public int width() {
		return image.getWidth();
	}

	public int height() {
		return image.getHeight();
	}

	public int rgb(int i, int j) {
		return image.getRGB(i, j);
	}

	public BufferedImage getImage() {
		return image;
	}

}
