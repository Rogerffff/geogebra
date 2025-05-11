package org.geogebra.PrintColorTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.freehep.graphics2d.PrintColor;
import org.junit.jupiter.api.Test;

public class GetColorDefaultModeTest {

	@Test
	public void testGetColorWithInvalidMode() {
		PrintColor pc = new PrintColor(0.3f, 0.3f, 0.3f, 1.0f, 0.5f, true);
		assertThrows(IllegalArgumentException.class, () -> {
			pc.getColor(99);
		});
	}
}