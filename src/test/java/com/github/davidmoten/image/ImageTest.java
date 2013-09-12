package com.github.davidmoten.image;

import static com.github.davidmoten.image.Pixels.alpha;
import static com.github.davidmoten.image.Pixels.blue;
import static com.github.davidmoten.image.Pixels.green;
import static com.github.davidmoten.image.Pixels.red;
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

		Image image = Image.fromClasspath("/tiny.gif");
		Pixels p = image.getPixels();
		assertEquals(8, p.width());
		assertEquals(8, p.height());
		{
			int value = p.rgb(0, 0);
			assertEquals(255, red(value));
			assertEquals(0, green(value));
			assertEquals(0, blue(value));
			assertEquals(255, alpha(value));
		}
		{
			int value = p.rgb(1, 0);
			assertEquals(0, red(value));
			assertEquals(255, green(value));
			assertEquals(0, blue(value));
			assertEquals(255, alpha(value));
		}
		{
			int value = p.rgb(2, 0);
			assertEquals(0, red(value));
			assertEquals(0, green(value));
			assertEquals(255, blue(value));
			assertEquals(255, alpha(value));
		}
		{
			int value = p.rgb(3, 0);
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
