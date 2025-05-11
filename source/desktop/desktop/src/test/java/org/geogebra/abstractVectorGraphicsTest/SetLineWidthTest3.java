package org.geogebra.abstractVectorGraphicsTest;

import org.junit.jupiter.api.Test;
import java.awt.Stroke;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.BasicStroke;

import static org.junit.jupiter.api.Assertions.*;

public class SetLineWidthTest3 {
	@Test
	public void testNonBasicStrokeShouldReplaceWithBasicStroke() {
		TestGraphics g = new TestGraphics();

		// 创建匿名 Stroke 类型（非 BasicStroke）
		Stroke customStroke = new Stroke() {
			@Override
			public Shape createStrokedShape(Shape p) {
				return new Rectangle2D.Double();
			}
		};

		g.mockStroke(customStroke);

		g.setLineWidth(2.5);

		assertTrue(g.getStroke() instanceof BasicStroke);
		assertEquals(2.5f, ((BasicStroke) g.getStroke()).getLineWidth());
	}
}
