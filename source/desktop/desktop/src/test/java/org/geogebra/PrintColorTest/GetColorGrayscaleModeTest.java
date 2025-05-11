package org.geogebra.PrintColorTest;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freehep.graphics2d.PrintColor;
import org.junit.jupiter.api.Test;

public class GetColorGrayscaleModeTest {

	@Test
	public void testGetColorWithGrayscaleMode() {
		PrintColor pc = new PrintColor(0.5f, 0.5f, 0.5f, 1.0f, 0.7f, false);
		PrintColor result = pc.getColor(PrintColor.GRAYSCALE);
		assertNotSame(pc, result);
		assertEquals(pc.getAsGray(), result.getAsGray(), 0.0001);
		assertEquals(pc.getAsBlack(), result.getAsBlack());
	}
}
