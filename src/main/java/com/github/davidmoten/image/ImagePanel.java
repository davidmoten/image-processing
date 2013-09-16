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
		INACTIVE, DRAWING_DIAMETER, ADJUSTING_CONTOUR, MOVING_ALONG_OBJECT
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
					state = State.ADJUSTING_CONTOUR;
				} else if (state == State.ADJUSTING_CONTOUR) {
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
				} else if (state == State.ADJUSTING_CONTOUR) {
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
		float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

		if (angle < 0) {
			angle += 360;
		}

		return angle;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this);
		g.setColor(Color.red);
		if (state == State.DRAWING_DIAMETER)
			g.drawLine(point1.x, point1.y, point2.x, point2.y);
		else if (state == State.ADJUSTING_CONTOUR) {
			drawContour(g, (point1.x + point2.x) / 2,
					(point1.y + point2.y) / 2, 360);
		} else if (state == State.MOVING_ALONG_OBJECT) {
			drawContour(g, (point1.x + point2.x) / 2,
					(point1.y + point2.y) / 2, 360);
			g.setColor(Color.green);
			g.drawLine(point3.x, point3.y, point4.x, point4.y);
			drawContour(g, point4.x, point4.y, 360);
		}
	}

	private void drawContour(Graphics g, int x, int y, int angle) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();

		float degrees = getAngle(point1.x, point1.y, point2.x, point2.y);
		System.out.println("degrees=" + degrees);
		AffineTransform aff = new AffineTransform();
		aff.translate(x, y);
		aff.rotate(Math.toRadians(degrees));
		g2d.transform(aff);
		// draw shape/image (will be rotated)
		Line2D.Float ln = new Line2D.Float(point1.x, point1.y, point2.x,
				point2.y);

		int distanceFromLine = (int) Math.round(ln.ptLineDist(point3.x,
				point3.y));
		int distance = (int) Math.round(distance(point1.x, point1.y, point2.x,
				point2.y));
		System.out.println(distanceFromLine + "," + distance);
		g.drawArc(-distance / 2, -distanceFromLine, distance,
				2 * distanceFromLine, 0, angle);
		g2d.setTransform(old);
	}

	private static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(sq(x1 - x2) + sq(y1 - y2));
	}

	private static float sq(float x) {
		return x * x;
	}
}
