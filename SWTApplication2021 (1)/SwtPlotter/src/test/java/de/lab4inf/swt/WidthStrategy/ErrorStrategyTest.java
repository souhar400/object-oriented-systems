package de.lab4inf.swt.WidthStrategy;

public class ErrorStrategyTest extends WidthStrategyTest {

	@Override
	public StepSizeStrategy createStrategy() {
		return new ErrorStepSizeStrategy();
	}

}
