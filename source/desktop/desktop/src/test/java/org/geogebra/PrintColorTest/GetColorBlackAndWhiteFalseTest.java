package org.geogebra.PrintColorTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.freehep.graphics2d.PrintColor;
import org.junit.jupiter.api.Test;

public class GetColorBlackAndWhiteFalseTest {

	@Test
	public void testGetColorWithBlackAndWhiteMode_WhenAsBlackIsFalse() {
		PrintColor pc = new PrintColor(0.3f, 0.3f, 0.3f, 1.0f, 0.8f, false);
		PrintColor result = pc.getColor(PrintColor.BLACK_AND_WHITE);
		PrintColor expected = new PrintColor(Color.white, pc.getAsGray(), pc.getAsBlack());
		assertEquals(expected, result);
	}
}
