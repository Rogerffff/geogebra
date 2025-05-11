package org.geogebra.PrintColorTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.freehep.graphics2d.PrintColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PrintColorTest {
	/* ---------- 构造器 & 边界检查 ---------- */

	@Test
	@DisplayName("构造器：灰阶值非法应抛异常")
	void constructor_invalidGray_throws() {
		assertThrows(IllegalArgumentException.class,
				() -> new PrintColor(1f, 1f, 1f, 2.0f, false));
	}

	@Test
	@DisplayName("createPrintColor：已是 PrintColor 时应原样返回")
	void createPrintColor_idempotent() {
		PrintColor pc = new PrintColor(Color.RED, 0.4f, false);
		assertSame(pc, PrintColor.createPrintColor(pc));
	}

	@Test
	@DisplayName("createPrintColor：普通 Color -> PrintColor")
	void createPrintColor_fromAwtColor() {
		PrintColor pc = PrintColor.createPrintColor(Color.GREEN);
		assertEquals(Color.GREEN.getRGB(), pc.getRGB());
	}

	/* ---------- getColor(mode) ---------- */

	@Nested
	class GetColor {

		private final PrintColor base = new PrintColor(1f, 0f, 0f, 0.5f, false); // 红色

		@Test
		void modeColor_returnsSelf() {
			assertSame(base, base.getColor(PrintColor.COLOR));
		}

		@Test
		void modeGray_returnsGrayInstance() {
			Color gray = base.getColor(PrintColor.GRAYSCALE);
			// 颜色通道 (0.5,0.5,0.5)，alpha 保留为 1.0f
			int expected = new Color(0.5f, 0.5f, 0.5f, 1.0f).getRGB();
			assertEquals(expected, gray.getRGB());
		}

		@Test
		void modeBW_blackBranch() {
			PrintColor black = new PrintColor(Color.BLACK, 0f, true);
			Color bw = black.getColor(PrintColor.BLACK_AND_WHITE);
			assertEquals(Color.BLACK.getRGB(), bw.getRGB());
		}

		@Test
		void modeBW_whiteBranch() {
			PrintColor white = new PrintColor(Color.WHITE, 1f, false);
			Color bw = white.getColor(PrintColor.BLACK_AND_WHITE);
			assertEquals(Color.WHITE.getRGB(), bw.getRGB());
		}

		@Test
		void modeInvalid_throws() {
			assertThrows(IllegalArgumentException.class,
					() -> base.getColor(99));
		}
	}

	/* ---------- 其他静态方法 ---------- */

	@Test
	void getDefaultColor_inRange() {
		Color c = PrintColor.getDefaultColor(0);
		assertNotNull(c);
	}

	@Test
	void getDefaultColor_outOfRange() {
		assertThrows(IllegalArgumentException.class,
				() -> PrintColor.getDefaultColor(999));
	}

	@Test
	void mixColor_avgRgb() {
		Color c1 = new Color(100, 100, 0);
		Color c2 = new Color(100, 200, 0);
		Color mixed = PrintColor.mixColor(c1, c2);
		assertEquals(new Color(100, 150, 0).getRGB(), mixed.getRGB());
	}

	/* ---------- equals / hashCode / invert ---------- */

	@Test
	void equals_and_hashCode() {
		PrintColor p1 = new PrintColor(Color.BLUE, 0.3f, true);
		PrintColor p2 = new PrintColor(Color.BLUE, 0.3f, true);
		assertEquals(p1, p2);
		assertEquals(p1.hashCode(), p2.hashCode());
	}

	@Test
	void invert_changesRgbGrayAndBlackness() {
		PrintColor red = new PrintColor(Color.RED, 0.25f, false);
		PrintColor inv = PrintColor.invert(red);
		assertNotEquals(red.getRGB(), inv.getRGB());
		assertEquals((red.getAsGray() + 0.5f) % 1.0f, inv.getAsGray(), 1e-6);
		assertNotEquals(red.getAsBlack(), inv.getAsBlack());
	}
}

