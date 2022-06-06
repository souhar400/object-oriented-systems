package de.lab4inf.swt.WidthStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.lab4inf.swt.plotter.JSEngine;
import de.lab4inf.swt.plotter.PlotterFunction;

public abstract class WidthStrategyTest {
	protected StepSizeStrategy strategy;
	private JSEngine parser = new JSEngine();
	private ConstantStepSizeStrategy cStrat = new ConstantStepSizeStrategy();
	private double xMin = -5.0, xMax = 5.0, yMin = -5.0, yMax = 5.0;
	private int hoehe = 1000, breite = 1000;
	@BeforeEach
	public void setUp() {
		strategy = createStrategy();
	}
	
	public abstract StepSizeStrategy createStrategy();
	
	@Test
	public void testLessPoints() {
		double[] pointsReturned, pointsConstant;
		PlotterFunction f = parser.parser("f(x)=x+1").getValue();
		pointsReturned = strategy.calculatePoints(f, xMin, xMax, yMin, yMax, breite, hoehe);
		pointsConstant = cStrat.calculatePoints(f, xMin, xMax, yMin, yMax, breite, hoehe);
		assert(pointsReturned.length < pointsConstant.length);
	}
}
