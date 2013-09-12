package com.github.davidmoten.image;

import java.io.IOException;

import org.junit.Test;

public class ImageTest {

	@Test
	public void test() throws IOException {

		Image image = Image.fromClasspath("/face.jpg");
		image.findBoundaries();
	}

}
