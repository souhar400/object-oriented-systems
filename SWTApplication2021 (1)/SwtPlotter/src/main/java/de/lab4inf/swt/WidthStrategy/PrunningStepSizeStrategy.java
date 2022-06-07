package de.lab4inf.swt.WidthStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;
import de.lab4inf.swt.plotter.Trafo;

public class PrunningStepSizeStrategy implements StepSizeStrategy {
	private static final double MIN_STEP_PX =1;
	private static final int MAX_STEP_PX = 18;
	protected static final int MAX_ITERATIONS = 25; 
	protected static final double PRUNING_FACTOR = 2.0;  

	@Override
	public double[] calculatePoints(PlotterFunction fct, double xMin, double xMax, double yMin, double yMax, int width, int hoehe) {
		double sizeWorld = xMax-xMin;
		double vScal = (yMax-yMin)/hoehe; 	
//		double delta = (xMin - xMax)/(hoehe); 
//		double delta =  1 / ((double) hoehe);
		double delta = vScal/(yMax-yMin); 
		
		double max_step = (sizeWorld / width) * MAX_STEP_PX ;
		double min_step = (sizeWorld / width)* MIN_STEP_PX;

		Function<Double, Double> toCalc = fct.getFunction();
		List<Double> pointslist = new ArrayList<>();
		double current = xMin;
		
		int[] point ; 
		double myY = toCalc.apply(current); 
		
		pointslist.add(current); 
		pointslist.add(toCalc.apply(current)); 
		
		double nextStep = min_step;  
		while(current < xMax) {	
			nextStep = calculateNextStep(toCalc, current, nextStep, max_step, delta);  
			current = current + nextStep; 
			
			pointslist.add(current); 
			pointslist.add(toCalc.apply(current)); 
			
			nextStep=min_step; 
		}
		double[] polygon = new double[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			polygon[i] = pointslist.get(i);
		return polygon;
	}

	protected double calculateNextStep(Function<Double, Double> function, double x, double lastStep, double max_step, double DELTA) {
		double nextStep = lastStep;
		double error;
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			error = calculateError(function, x, nextStep);
			if (Math.abs(error) >= DELTA) {
				return nextStep;
			}
			nextStep *= PRUNING_FACTOR;
			if (nextStep >= max_step) {
				return max_step;
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