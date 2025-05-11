package org.geogebra.PrintColorTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.freehep.graphics2d.PrintColor;
import org.junit.jupiter.api.Test;

public class GetColorBlackAndWhiteTrueTest {

	@Test
	public void testGetColorWithBlackAndWhiteMode_WhenAsBlackIsTrue() {
		PrintColor pc = new PrintColor(0.3f, 0.3f, 0.3f, 1.0f, 0.8f, true);
		PrintColor result = pc.getColor(PrintColor.BLACK_AND_WHITE);
		PrintColor expected = new PrintColor(Color.black, pc.getAsGray(), pc.getAsBlack());
		assertEquals(expected, result);
	}
}