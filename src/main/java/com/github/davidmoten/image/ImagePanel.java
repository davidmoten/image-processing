package com.github.davidmoten.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -4425842629963009798L;
	private final BufferedImage image;

	private enum State {
		INACTIVE, DRAWING_DIAMETER, ADJUSTING_SIZE, MOVING_ALONG_OBJECT
	}

	private State state = State.INACTIVE;
	private Point point1, point2, point3, point4;

	public ImagePanel(BufferedImage image) {
		this.image = image;
		setLayout(new BorderLayout());
		setSize(image.getWidth(), image.getHeight());
		MouseAdapter mouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(e.getPoint());
				if (state == State.INACTIVE) {
					point1 = e.getPoint();
					state = State.DRAWING_DIAMETER;
					point2 = point1;
				} else if (state == State.DRAWING_DIAMETER) {
					point2 = e.getPoint();
					point3 = point2;
					state = State.ADJUSTING_SIZE;
				} else if (state == State.ADJUSTING_SIZE) {
					point3 = e.getPoint();
					state = State.MOVING_ALONG_OBJECT;
				} else if (state == State.MOVING_ALONG_OBJECT) {
					point4 = e.getPoint();
					state = State.INACTIVE;
				}
				repaint();
			}

		};
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (state == State.DRAWING_DIAMETER) {
					point2 = e.getPoint();
					repaint();
				} else if (state == State.ADJUSTING_SIZE) {
					point3 = e.getPoint();
					repaint();
				} else if (state == State.MOVING_ALONG_OBJECT) {
					point4 = e.getPoint();
					repaint();
				}
			}
		});
	}

	public static float getAngle(int x1, int y1, int x2, int y2) {
		float angle = (float) Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));

		if (angle < 0) {
			angle += 360;
		}

		return angle;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g.drawImage(image, 0, 0, this);
		g.setColor(Color.red);
		if (state == State.DRAWING_DIAMETER)
			g.drawLine(point1.x, point1.y, point2.x, point2.y);
		else if (state == State.ADJUSTING_SIZE) {

			AffineTransform old = g2d.getTransform();
			float degrees = getAngle(point1.x, point1.y, point2.x, point2.y);
			g2d.translate(point1.x, point1.y);
			g2d.rotate(Math.toRadians(degrees));
			// draw shape/image (will be rotated)
			Line2D.Float ln = new Line2D.Float(point1.x, point1.y, point2.x,
					point2.y);
			int distanceFromLine = (int) Math.round(ln.ptLineDist(point3.x,
					point3.y));
			int distance = (int) Math.round(distance(point1.x, point1.y,
					point2.x, point2.y));
			System.out.println(distanceFromLine + "," + distance);
			g.drawOval(0, 0, distance, distanceFromLine);
			g2d.setTransform(old);
			// things you draw after here will not be rotated
			g.drawOval(ALLBITS, ABORT, WIDTH, HEIGHT);
		}

	}

	private static double distance(int x, int y, int x2, int y2) {
		return Math.sqrt(sq(x - x2) + sq(y - y2));
	}

	private static float sq(float x) {
		return x * x;
	}
}
