package org.geogebra.abstractVectorGraphicsTest;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import org.freehep.graphics2d.AbstractVectorGraphics;

public class TestGraphics extends AbstractVectorGraphics {
	private Stroke stroke;
	@Override
	public Stroke getStroke() {
		return stroke;
	}

	@Override
	public void setStroke(Stroke s) {
		this.stroke = s;
	}


	public void mockStroke(Stroke s) {
		this.stroke = s;
	}
	@Override
	protected Shape createShape(double[] xPoints, double[] yPoints, int nPoints, boolean close) {
		return null;
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {

	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {

	}

	@Override
	public Graphics create() {
		return null;
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean drawImage(Image image, int x, int y, ImageObserver observer) {
		return false;
	}

	@Override
	public boolean drawImage(Image image, int x, int y, int width, int height,
			ImageObserver observer) {
		return false;
	}

	@Override
	public boolean drawImage(Image image, int x, int y, Color bgColor, ImageObserver observer) {
		return false;
	}

	@Override
	public boolean drawImage(Image image, int x, int y, int width, int height, Color bgColor,
			ImageObserver observer) {
		return false;
	}

	@Override
	public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1,
			int sx2, int sy2, ImageObserver observer) {
		return false;
	}

	@Override
	public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1,
			int sx2, int sy2, Color bgColor, ImageObserver observer) {
		return false;
	}

	@Override
	public Shape getClip() {
		return null;
	}

	@Override
	public Rectangle getClipBounds() {
		return null;
	}

	@Override
	public Rectangle getClipBounds(Rectangle r) {
		return null;
	}

	@Override
	public FontMetrics getFontMetrics(Font font) {
		return null;
	}

	@Override
	public void setClip(int x, int y, int width, int height) {

	}

	@Override
	public void setClip(Shape clip) {

	}

	@Override
	public void setPaintMode() {

	}

	@Override
	public void setXORMode(Color c1) {

	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public void addRenderingHints(Map hints) {

	}

	@Override
	public void clip(Shape s) {

	}

	@Override
	public void draw(Shape s) {

	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {

	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {

	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return false;
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {

	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {

	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {

	}

	@Override
	public void fill(Shape s) {

	}

	@Override
	public Composite getComposite() {
		return null;
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return null;
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return null;
	}

	@Override
	public Object getRenderingHint(RenderingHints.Key inteKey) {
		return null;
	}

	@Override
	public RenderingHints getRenderingHints() {
		return null;
	}



	@Override
	public AffineTransform getTransform() {
		return null;
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return false;
	}

	@Override
	public void rotate(double theta) {

	}

	@Override
	public void scale(double sx, double sy) {

	}

	@Override
	public void setComposite(Composite comp) {

	}

	@Override
	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {

	}

	@Override
	public void setRenderingHints(Map hints) {

	}



	@Override
	public void setTransform(AffineTransform xform) {

	}

	@Override
	public void shear(double shx, double shy) {

	}

	@Override
	public void transform(AffineTransform xform) {

	}

	@Override
	public void translate(double tx, double ty) {

	}

	@Override
	public void clearRect(double x, double y, double width, double height) {

	}

	@Override
	public void clipRect(double x, double y, double width, double height) {

	}

	@Override
	public Graphics create(double x, double y, double width, double height) {
		return null;
	}

	@Override
	public void drawString(String str, double x, double y) {

	}

	@Override
	public void endExport() {

	}

	@Override
	public void printComment(String comment) {

	}

	@Override
	public void setClip(double x, double y, double width, double height) {

	}

	@Override
	public void startExport() {

	}
}
