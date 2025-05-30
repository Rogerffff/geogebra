package org.geogebra.common.main.settings.config;

import org.geogebra.common.GeoGebraConstants;
import org.geogebra.common.io.layout.Perspective;
import org.geogebra.common.kernel.commands.filter.CommandArgumentFilter;
import org.geogebra.common.kernel.commands.selector.CommandFilter;
import org.geogebra.common.kernel.commands.selector.CommandFilterFactory;

/**
 * Config for Evaluator
 *
 */
public class AppConfigEvaluator extends AppConfigDefault {

	public AppConfigEvaluator() {
		super(GeoGebraConstants.EVALUATOR_APPCODE);
	}

	@Override
	public String getForcedPerspective() {
		return Perspective.EVALUATOR + "";
	}

	@Override
	public String getAppTitle() {
		return "Evaluator";
	}

	@Override
	public String getTutorialKey() {
		return "evaluator_tutorials";
	}

	@Override
	public boolean isCASEnabled() {
		return false;
	}

	@Override
	public CommandFilter createCommandFilter() {
		return CommandFilterFactory.createNoCasCommandFilter();
	}

	@Override
	public CommandArgumentFilter getCommandArgumentFilter() {
		return null;
	}

	@Override
	public boolean shouldHideEquations() {
		return false;
	}

	@Override
	public boolean sendKeyboardEvents() {
		return true;
	}

	@Override
	public boolean hasLabelForDescription() {
		return true;
	}
}
