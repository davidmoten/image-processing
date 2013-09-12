package com.github.davidmoten.image;

public class Line {
	private final int x1, y1, x2, y2;
	private final float m;
	private final float c;

	public Line(int x1, int y1, int x2, int y2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		m = (y2 - y1) / (float) (x2 - x1);
		c = y2 - m * x2;
		// y = mx + c
	}

	public int x1() {
		return x1;
	}

	public int y1() {
		return y1;
	}

	public int x2() {
		return x2;
	}

	public int y2() {
		return y2;
	}

	public float distance(int x, int y) {
		return (float) (Math.abs((x2-x1)*(y1-y)-(x1-x)*(y2-y1))/Math.sqrt(sq(x2-x1)+ sq(y2-y2)));
	}

	private double sq(double d) {
		return d * d;
	}

}
