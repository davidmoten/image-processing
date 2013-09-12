package com.github.davidmoten.image;

import ij.process.ColorProcessor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Image {

	private static final boolean useCanny = false;
	private final Pixels pixels;
	private final BufferedImage image;

	public Pixels getPixels() {
		return pixels;
	}

	public Image(BufferedImage image) {
		this.image = image;
		pixels = new Pixels(image);
	}

	public static Image fromClasspath(String path) {
		try {
			return new Image(
					ImageIO.read(Image.class.getResourceAsStream(path)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void findBoundaries(Line line, int maxDistanceToLine) {
		log("finding boundaries for " + pixels.width() * pixels.height()
				+ " pixels");
		long t = System.currentTimeMillis();
		Line2D segment = new Line2D.Double(new Point2D.Double(
				line.x1(), line.y1()), new Point2D.Double(line.x2(),
				line.y2()));
		for (int x = 0; x < pixels.width(); x++)
			for (int y = 0; y < pixels.height(); y++) {
				int rgb = pixels.rgb(x, y);
				int red = Pixels.red(rgb);
				int green = Pixels.green(rgb);
				int blue = Pixels.blue(rgb);
				
				double distance = segment.ptSegDist(new Point2D.Double(x,y));
				double factor = Math.max(0, maxDistanceToLine - distance)
						/ maxDistanceToLine;

				float[] hsb = new float[3];
				Color.RGBtoHSB(red, green, blue, hsb);
				int color = Color.HSBtoRGB(hsb[0], hsb[1], (float) (hsb[2]
						* factor * factor));

				pixels.setRGB(x, y, color);
				// pixels.setRGB(x, y, new Color(green, blue, 255 -
				// red).getRGB());
			}
		log("found boundaries time ms = " + (System.currentTimeMillis() - t));
		display(image);

		int minX = Math.min(line.x1(), line.x2());
		int maxX = Math.max(line.x1(), line.x2());
		int minY = Math.min(line.y1(), line.y2());
		int maxY = Math.max(line.y1(), line.y2());

		BufferedImage subImage = image.getSubimage(
				Math.max(0, minX - maxDistanceToLine),
				Math.max(0, minY - maxDistanceToLine), maxX - minX
						+ maxDistanceToLine, maxY - minY + maxDistanceToLine);

		BufferedImage edges;
		if (useCanny) {
			CannyEdgeDetector detector = new CannyEdgeDetector();

			detector.setLowThreshold(0.5f);
			detector.setHighThreshold(1.0f);

			detector.setSourceImage(subImage);
			detector.setContrastNormalized(false);
			detector.process();
			edges = detector.getEdgesImage();
		} else {
			// use ImageJ
			ColorProcessor ip = new ColorProcessor(subImage);
			ip.findEdges();
			edges = ip.getBufferedImage();
		}
		display(edges);
	}

	public static void display(final BufferedImage image) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.setSize(image.getWidth(), image.getHeight());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout());
				JLabel label = new JLabel("");
				label.setIcon(new javax.swing.ImageIcon(image));
				panel.add(label);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	private void log(String message) {
		System.out.println(message);
	}

}
