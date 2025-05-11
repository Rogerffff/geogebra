package org.geogebra.abstractVectorGraphicsTest;

import org.junit.jupiter.api.Test;
import java.awt.BasicStroke;

import static org.junit.jupiter.api.Assertions.*;

public class SetLineWidthTest2 {
	@Test
	public void testSameWidthShouldNotUpdateStroke() {
		TestGraphics g = new TestGraphics();
		BasicStroke original = new BasicStroke(3.0f); // 当前宽度为 3.0
		g.mockStroke(original);

		g.setLineWidth(3.0); // 设置相同宽度

		assertSame(original, g.getStroke()); // 不应更换 stroke
	}
}
