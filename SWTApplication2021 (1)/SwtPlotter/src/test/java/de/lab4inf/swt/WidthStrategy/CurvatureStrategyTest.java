package de.lab4inf.swt.WidthStrategy;

public class CurvatureStrategyTest extends WidthStrategyTest {

	@Override
	public StepSizeStrategy createStrategy() {
		return new CurvatureStepSizeStrategy();
	}

}
