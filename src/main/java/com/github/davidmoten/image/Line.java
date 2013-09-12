package com.github.davidmoten.image;

public class Line {
	private final int x1, y1, x2, y2;

	public Line(int x1, int y1, int x2, int y2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
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

}
