package org.geogebra.abstractVectorGraphicsTest;

import org.junit.jupiter.api.Test;
import java.awt.BasicStroke;

import static org.junit.jupiter.api.Assertions.*;

public class SetLineWidthTest1 {
	@Test
	public void testDifferentWidthShouldUpdateStroke() {
		TestGraphics g = new TestGraphics();
		BasicStroke original = new BasicStroke(2.0f); // 当前宽度 2.0
		g.mockStroke(original);

		g.setLineWidth(5.0); // 设置为不同线宽

		assertTrue(g.getStroke() instanceof BasicStroke);
		assertNotSame(original, g.getStroke()); // 应该是新的 stroke
		assertEquals(5.0f, ((BasicStroke) g.getStroke()).getLineWidth());
	}
}
