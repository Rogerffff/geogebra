package org.geogebra.PrintColorTest;

import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

import org.freehep.graphics2d.PrintColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test: AbstractVectorGraphics#getPrintColor
 * relies on PrintColor and internal colorMode.
 */
class AbstractVectorGraphicsPrintColorTest {

	private DummyVectorGraphics g;
	private final PrintColor red = new PrintColor(Color.RED, 0.3f, false);

	@BeforeEach
	void setUp() {
		g = new DummyVectorGraphics();
	}

	@Test
	void colorMode_COLOR_returnsOriginal() {
		g.setColorMode(PrintColor.COLOR);
		assertSame(red, g.getPrintColorPublic(red));
	}

	@Test
	void colorMode_GRAYSCALE_converts() {
		g.setColorMode(PrintColor.GRAYSCALE);
		Color gray = g.getPrintColorPublic(red);
		assertEquals(new Color(red.getAsGray(), red.getAsGray(), red.getAsGray()).getRGB(),
				gray.getRGB());
	}

	@Test
	void colorMode_BW_converts() {
		g.setColorMode(PrintColor.BLACK_AND_WHITE);
		Color bw = g.getPrintColorPublic(red);
		int rgb = bw.getRGB();
		assertTrue(rgb == Color.WHITE.getRGB() || rgb == Color.BLACK.getRGB());
	}
}
