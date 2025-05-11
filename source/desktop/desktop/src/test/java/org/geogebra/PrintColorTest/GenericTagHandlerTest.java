package org.geogebra.PrintColorTest;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import org.freehep.graphics2d.PrintColor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.freehep.graphics2d.PrintColor;   // ← 加这一行

import org.freehep.graphics2d.GenericTagHandler;
import org.freehep.graphics2d.TagString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GenericTagHandlerTest {
	/** 真实 Graphics2D，Spy 后可以校验 drawString 调用 */
	private Graphics2D g2dSpy;
	private GenericTagHandler handler;

	@BeforeEach
	void setUp() {
		// 用一个 10×10 的空 BufferedImage 生成 Graphics2D
		BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D raw = img.createGraphics();
		raw.setFont(new Font("Serif", Font.PLAIN, 12));

		g2dSpy = Mockito.spy(raw);
		handler = new GenericTagHandler(g2dSpy);
	}

	/* ---------- createTextLayout() 相关 ---------- */

	@Test
	@DisplayName("标签应被完全剥离")
	void layoutStripsAllTags() {
		TagString tagged = new TagString("<b>Hello</b>");
		TextLayout tl = handler.createTextLayout(tagged, 0);
		assertEquals(5, tl.getCharacterCount(), "字符总数应为 5");
	}

	@Test
	@DisplayName("嵌套标签也能正确剥离")
	void layoutHandlesNestedTags() {
		TagString tagged = new TagString("<b>He<i>ll</i>o</b>");
		TextLayout tl = handler.createTextLayout(tagged, 0);
		assertEquals(5, tl.getCharacterCount());
	}

	@Test
	@DisplayName("sup / sub 标签不抛异常")
	void layoutHandlesSupAndSub() {
		TagString tagged = new TagString("x<sup>2</sup> + y<sub>1</sub>");
		assertDoesNotThrow(() -> handler.createTextLayout(tagged, 1.0));
	}

	/* ---------- print() 相关 ---------- */

	@Test
	@DisplayName("print() 应当调用一次 drawString()")
	void printInvokesDrawStringExactlyOnce() {
		TagString tagged = new TagString("<u>Hi</u>");

		handler.print(tagged, 15, 30, 0);

		// 捕获调用参数，验证位置以及迭代器里的文本
		ArgumentCaptor<AttributedCharacterIterator> itCaptor = ArgumentCaptor.forClass(AttributedCharacterIterator.class);
		verify(g2dSpy, times(1))
				.drawString(itCaptor.capture(), eq(15f), eq(30f));

		AttributedCharacterIterator it = itCaptor.getValue();
		StringBuilder sb = new StringBuilder();
		for (char c = it.first(); c != AttributedCharacterIterator.DONE; c = it.next()) {
			sb.append(c);
		}
		assertEquals("Hi", sb.toString());
	}

	/* ---------- 集成 PrintColor / colorMode ---------- */

	@Test
	@DisplayName("灰度模式下依旧可以 print()")
	void printWorksInGrayScaleMode() {
		DummyVectorGraphics vg = new DummyVectorGraphics();
		vg.setFont(new Font("Serif", Font.PLAIN, 12));
		vg.setColorMode(PrintColor.GRAYSCALE);       // ← 与底层 PrintColor 互动
		GenericTagHandler localHandler = new GenericTagHandler(vg);
		assertDoesNotThrow(() -> localHandler.print(new TagString("Gray"), 0, 0, 0));
	}
}

