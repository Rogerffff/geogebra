package org.geogebra.web.full.gui.color;

import java.util.HashMap;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.gui.util.SelectionTable;
import org.geogebra.common.main.GeoGebraColorConstants;
import org.geogebra.web.full.css.MaterialDesignResources;
import org.geogebra.web.full.gui.util.GeoGebraIconW;
import org.geogebra.web.full.gui.util.PopupMenuButtonW;
import org.geogebra.web.html5.gui.util.ImageOrText;
import org.geogebra.web.html5.main.AppW;
import org.gwtproject.event.dom.client.ClickEvent;
import org.gwtproject.event.dom.client.ClickHandler;

/**
 * Color popup for stylebar
 */
public class ColorPopupMenuButton extends PopupMenuButtonW implements ClickHandler {
	/** foreground */
	public static final int COLORSET_DEFAULT = 0;
	/** background */
	public static final int COLORSET_BGCOLOR = 1;
	private final int colorSetType;
	private final GColor[] colorSet;
	private GColor defaultColor;
	private final HashMap<GColor, Integer> lookupMap;

	private boolean enableTable;
	private boolean hasSlider;

	/**
	 * @param app
	 *            {@link AppW}
	 * @param colorSetType
	 *            {@code int}
	 * @param hasSlider
	 *            provides geos to make slider action undoable
	 */
	public ColorPopupMenuButton(AppW app, int colorSetType,
			boolean hasSlider) {
		super(app, createDummyIcons(10), -1, 5, SelectionTable.MODE_ICON, true, hasSlider);
		this.app = app;
		this.colorSetType = colorSetType;
		this.hasSlider = hasSlider;

		colorSet = getColorSet();
		defaultColor = colorSet[0];

		lookupMap = new HashMap<>();
		for (int i = 0; i < colorSet.length; i++) {
			if (colorSet[i] != null) {
				lookupMap.put(colorSet[i], i);
			}
		}
		if (hasSlider) {
			getSlider().setMinimum(0);
			getSlider().setMaximum(100);
			getSlider().setTickSpacing(5);
			setSliderValue(100);
		}

		updateColorTable();
		setKeepVisible(false);
		getMyTable().removeDefaultStyle();
	}

	/**
	 * Sets the colors to choose from.
	 */
	protected GColor[] getColorSet() {
		return GeoGebraColorConstants.getSimplePopupArray();
	}

	/**
	 * @param visible
	 *            {@code boolean}
	 */
	protected void setSliderVisible(boolean visible) {
		hasSlider = visible;
		showSlider(hasSlider);
	}

	/**
	 * Update the table
	 */
	protected void updateColorTable() {
		getMyTable().populateModel(
				getColorSwatchIcons(colorSet, getSliderValue() / 100f));
	}

	@Override
	public ImageOrText getButtonIcon() {
		ImageOrText icon = super.getButtonIcon();
		if (icon == null) {
			icon = GeoGebraIconW.createColorSwatchIcon(getSliderValue() / 100f,
					defaultColor, null);
		}
		return icon;
	}

	/**
	 * @param color
	 *            {@link GColor}
	 * @return {@code int} the index of the given color
	 */
	protected int getColorIndex(GColor color) {
		int index = -1;

		if (color == null && colorSetType == COLORSET_BGCOLOR) {
			index = colorSet.length - 1;
			return index;
		}

		if (lookupMap.containsKey(color)) {
			index = lookupMap.get(color);
		}

		return index;
	}

	/**
	 * @return the selected {@link GColor color}
	 */
	public GColor getSelectedColor() {
		if (!enableTable) {
			return null;
		}
		int index = getSelectedIndex();
		if (index <= -1) {
			return defaultColor;
		} else if (colorSetType == COLORSET_BGCOLOR
				&& index > colorSet.length - 1) {
			return null;
		} else {
			return colorSet[index];
		}
	}

	/**
	 * @param alpha
	 *            opacity
	 * @param gc
	 *            base color
	 */
	protected void setDefaultColor(double alpha, GColor gc) {
		defaultColor = gc;
		if (gc != null) {
			this.setIcon(GeoGebraIconW.createColorSwatchIcon(alpha, gc, null));
			this.getElement().getStyle().setBorderColor(gc.toString());
		} else {
			this.setIcon(GeoGebraIconW.createNullSymbolIcon());
			this.getElement().getStyle().setBorderColor(GColor.BLACK.toString());
		}
	}

	private static ImageOrText[] getColorSwatchIcons(GColor[] colorArray,
			double alpha) {
		ImageOrText[] a = new ImageOrText[colorArray.length];
		for (int i = 0; i < colorArray.length; i++) {
			if (colorArray[i] != null) {
				a[i] = GeoGebraIconW.createColorSwatchIcon(alpha, colorArray[i],
						null);
			} else {
				a[i] = new ImageOrText(
						MaterialDesignResources.INSTANCE.add_black(), 24)
								.setClass("plusButton");
				// a[i] = new ImageOrText("+");
				// a[i] = new ImageOrText(AppResources.INSTANCE.more());
			}
		}
		return a;
	}

	private static ImageOrText[] createDummyIcons(int count) {
		ImageOrText[] a = new ImageOrText[count];
		for (int i = 0; i < count; i++) {
			a[i] = new ImageOrText();
		}
		return a;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (this.hasSlider) {
			int si = getSelectedIndex();
			defaultColor = getSelectedColor();
			updateColorTable();
			setSelectedIndex(si);
		}
	}

	@Override
	protected void fireActionPerformed() {
		if (this.hasSlider) {
			int si = getSelectedIndex();
			defaultColor = getSelectedColor();
			updateColorTable();
			setSelectedIndex(si);
		}
	}

	/**
	 * @return whether table is enabled (otherwise just slider)
	 */
	public boolean isEnableTable() {
		return enableTable;
	}

	/**
	 * @param enableTable
	 *            whether table is enabled (otherwise just slider)
	 */
	public void setEnableTable(boolean enableTable) {
		this.enableTable = enableTable;
		getMyTable().setVisible(enableTable);
	}
}
