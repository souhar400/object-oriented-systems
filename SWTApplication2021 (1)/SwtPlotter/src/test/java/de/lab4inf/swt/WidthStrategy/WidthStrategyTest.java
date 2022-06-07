package de.lab4inf.swt.WidthStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.lab4inf.swt.plotter.JSEngine;
import de.lab4inf.swt.plotter.PlotterFunction;

public abstract class WidthStrategyTest {
	protected StepSizeStrategy strategy;
	private JSEngine parser = new JSEngine();
	private ConstantStepSizeStrategy cStrat = new ConstantStepSizeStrategy();
	private double xMin = -5.0, xMax = 5.0, yMin = -5.0, yMax = 5.0;
	private int hoehe = 1000, breite = 2000;
	double vMax= hoehe;
	double vScal = vMax/(yMax-yMin); 
	private double error = vScal/vMax;
	private double tolerance = ((xMax - xMin) / ((double) breite))*1.05;  //  =1/uScal
	
	
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
	
	@Test
	public void testIdFunction() {
		Function<Double, Double> func = x -> x;
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	@Test
	public void testNegIdFunction() {
		Function<Double, Double> func = x -> -x;
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}
	
	@Test
	public void geradeFunction() {
		Function<Double, Double> func = x -> ( 2*x-5) ;
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	@Test
	public void testInverseXSin() {
		Function<Double, Double> func = x -> Math.sin(1 / x);
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	@Test
	public void testInverseXCos() {
		Function<Double, Double> func = x -> Math.cos(1 / x);
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}
	
	@Test
	public void testLn() {
		Function<Double, Double> func = x -> Math.log(x);
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	@Test
	public void testSinX3() {
		Function<Double, Double> func = x -> Math.sin(x*x*x);
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}
	
	
	@Test
	public void testComplexTan() {
		Function<Double, Double> func = x -> Math.tan(Math.sin(1 / x));
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	@Test
	public void testComplexFunction() {
		Function<Double, Double> func = x -> Math.sin(Math.cos(x));
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	@Test
	public void testAsinFunction() {
		Function<Double, Double> func = x -> Math.asin(x);
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	@Test
	public void testAcosFunction() {
		Function<Double, Double> func = x -> Math.acos(x);
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	

	@Test
	public void testExp() {
		Function<Double, Double> func = x -> Math.exp(x);
		PlotterFunction myFct = new PlotterFunction(func, "testFct", new int[] { 255, 255, 255 }, 1);
		check(myFct);
	}

	private void check(PlotterFunction func) {
		double[] notFiltered = strategy.calculatePoints(func, xMin, xMax, yMin, yMax, breite, hoehe);
		double[] xy= filterPoints(notFiltered);
		for (int i = 0; i < xy.length - 3; i = i + 2) {
			double first = xy[i];
			double second = xy[i + 2];
			double mid = (first + second) / 2;
			double expected = func.getFunction().apply(mid);
			if ((second - first) > tolerance ) {
				assertEquals(expected, (xy[i + 1] + xy[i + 3]) / 2, error);
			}
		}

	}
	
	private double[] filterPoints(double[] polygon) {

		List<Double> pointslist = new ArrayList<>();

		for (int j = 0; j <= polygon.length - 2; j = j + 2) {
			double myY = polygon[j + 1];

			if (!(Double.isNaN(myY) || (int) ((hoehe/2)  - myY) > hoehe || ((hoehe/2) - myY) < -hoehe)) {
				Collections.addAll(pointslist, polygon[j], polygon[j+1]);
			}
		}

		double[] result = new double[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			result[i] = pointslist.get(i);
		return result;
	}
	
	
}
