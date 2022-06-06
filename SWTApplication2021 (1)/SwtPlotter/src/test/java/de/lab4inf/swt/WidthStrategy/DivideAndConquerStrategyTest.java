package de.lab4inf.swt.WidthStrategy;

public class DivideAndConquerStrategyTest extends WidthStrategyTest {

	@Override
	public StepSizeStrategy createStrategy() {
		return new DivideAndConquerStepSizeStrategy();
	}

}
