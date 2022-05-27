package de.lab4inf.swt.WidthStrategy;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.cairo.cairo_font_extents_t;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;
import de.lab4inf.swt.plotter.Trafo;

public class DivideAndConquerStepSizeStrategy implements StepSizeStrategy {
	private static final double MIN_STEP_PX =1;
	private static final int MAX_STEP_PX = 25;
	protected static final int MAX_ITERATIONS = 25; //25
	protected static final double DIVIDING_FACTOR = 0.5;  //1.1

	@Override
	public int[] calculatePoints(SWTCanvasPlotter canvas, PlotterFunction fct) {
		int sizeScreen = canvas.getMaxU();
		double sizeWorld = canvas.getIntervall()[1]-canvas.getIntervall()[0];
		Trafo transformer = new Trafo(canvas);
		double delta = ((canvas.getyIntervall()[1]-canvas.getyIntervall()[0])/(canvas.getMaxV())) ; 
		double hoehe = canvas.getMaxV();

		
		double max_step = sizeWorld / sizeScreen * MAX_STEP_PX ;
		double min_step = sizeWorld / sizeScreen * MIN_STEP_PX;
		System.out.println("max_Step : "+ max_step);
		System.out.println("min_step : "+ min_step);

		Function<Double, Double> toCalc = fct.getFunction();
		List<Integer> pointslist = new ArrayList<>();
		double current = canvas.getIntervall()[0];
		
		int[] point ; 
		double myY = toCalc.apply(current); 
		if (!(Double.isNaN(myY) || (int) (canvas.getYOrigin() - myY) > hoehe || (int) (canvas.getYOrigin() - myY) < -hoehe))
		{
			point= transformer.convertXY(current, toCalc.apply(current));
			Collections.addAll(pointslist, point[0], point[1]);
		}
		double nextStep = max_step;  
		while(current < canvas.getIntervall()[1]) {	
			nextStep = calculateByDivideAndConquer(toCalc, current, nextStep, min_step, delta);  
			current = current + nextStep; 
			
			myY = toCalc.apply(current); 
			if (!(Double.isNaN(myY) || (int) (canvas.getYOrigin() - myY) > hoehe || (int) (canvas.getYOrigin() - myY) < -hoehe))
			{
				point= transformer.convertXY(current, toCalc.apply(current));
				Collections.addAll(pointslist, point[0], point[1]);
			}
			nextStep=max_step; 
		}
		int[] polygon = new int[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			polygon[i] = pointslist.get(i);
		return polygon;
	}

	protected double calculateByDivideAndConquer(Function<Double, Double> function, double x, double lastStep, double min_step, double DELTA) {
		double nextStep = lastStep;
		double error;
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			error = calculateError(function, x, nextStep);
			if (Math.abs(error) < DELTA) {
				return nextStep;
			}
			nextStep *= DIVIDING_FACTOR;
			if (nextStep <= min_step) {
				return min_step;
			}
		}
		return nextStep;
	}
	
	protected double calculateError(Function<Double, Double> function, double x, double nextStep) {
		double y_minus_step = function.apply(x - nextStep);
		double y = function.apply(x);
		double y_plus_step = function.apply(x + nextStep);
		double error = (y_minus_step - 2 * y + y_plus_step);
		return Math.abs(error);
	}
}
