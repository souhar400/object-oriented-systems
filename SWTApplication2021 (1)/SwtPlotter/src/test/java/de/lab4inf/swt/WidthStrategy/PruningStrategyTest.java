package de.lab4inf.swt.WidthStrategy;

public class PruningStrategyTest extends WidthStrategyTest {

	@Override
	public StepSizeStrategy createStrategy() {
		return new PrunningStepSizeStrategy();
	}

}
