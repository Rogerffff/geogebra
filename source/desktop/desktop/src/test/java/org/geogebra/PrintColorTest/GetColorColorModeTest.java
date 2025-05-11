package org.geogebra.PrintColorTest;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.freehep.graphics2d.PrintColor;
import org.junit.jupiter.api.Test;

public class GetColorColorModeTest {

	@Test
	public void testGetColorWithColorMode() {
		PrintColor pc = new PrintColor(0.5f, 0.5f, 0.5f, 1.0f, 0.5f, true);
		PrintColor result = pc.getColor(PrintColor.COLOR);
		assertSame(pc, result);
	}
}