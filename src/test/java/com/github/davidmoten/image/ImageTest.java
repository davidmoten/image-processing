package com.github.davidmoten.image;

import static com.github.davidmoten.image.Image.alpha;
import static com.github.davidmoten.image.Image.blue;
import static com.github.davidmoten.image.Image.green;
import static com.github.davidmoten.image.Image.red;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class ImageTest {

	@Test
	public void test() {

		Image image = Image.fromClasspath("/face.jpg");
		image.findBoundaries(new Line(46, 130, 220, 80), 50);
	}

	@Test
	public void testTinyGifRedGreenBlueAlpha() throws IOException {

		Image im = Image.fromClasspath("/tiny.gif");
		assertEquals(8, im.width());
		assertEquals(8, im.height());
		{
			int value = im.rgb(0, 0);
			assertEquals(255, red(value));
			assertEquals(0, green(value));
			assertEquals(0, blue(value));
			assertEquals(255, alpha(value));
		}
		{
			int value = im.rgb(1, 0);
			assertEquals(0, red(value));
			assertEquals(255, green(value));
			assertEquals(0, blue(value));
			assertEquals(255, alpha(value));
		}
		{
			int value = im.rgb(2, 0);
			assertEquals(0, red(value));
			assertEquals(0, green(value));
			assertEquals(255, blue(value));
			assertEquals(255, alpha(value));
		}
		{
			int value = im.rgb(3, 0);
			assertEquals(255, red(value));
			assertEquals(255, green(value));
			assertEquals(255, blue(value));
			assertEquals(0, alpha(value));
		}
	}

	public static void main(String[] args) throws IOException {
		Image image = Image.fromClasspath("/telescope.png");
		image.findBoundaries(new Line(75, 120, 285, 40), 30);
	}
}
