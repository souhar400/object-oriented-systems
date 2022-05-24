package de.lab4inf.swt.WidthStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;
import de.lab4inf.swt.plotter.Trafo;

public class PrunningStepSizeStrategy2 implements StepSizeStrategy {
	private static final double MIN_STEP_PX =1;
	private static final int MAX_STEP_PX = 50;
	protected static final int MAX_ITERATIONS = 25;
	protected static final double PRUNING_FACTOR = 1.1;
	protected static final double DIVIDE_FACTOR = 0.9;
	protected static double DELTA;  //TODO Delta richtig berechnen
	
	@Override
	public int[] calculatePoints(SWTCanvasPlotter canvas, PlotterFunction fct) {
		//DELTA = canvas.vScal/canvas.getMaxV(); 
		DELTA=0.05; 
		int sizeScreen = canvas.getMaxU();
		double sizeWorld = canvas.getIntervall()[1]-canvas.getIntervall()[0];
		Trafo transformer = new Trafo(canvas);
		
//		double max_step=1; 
		double min_step= sizeWorld/sizeScreen; 
//		double min_step = sizeWorld / (sizeScreen * MIN_STEP_PX);
		double max_step = sizeWorld / (sizeScreen * MAX_STEP_PX) ;
		double error =1/sizeScreen;  
		Function<Double, Double> toCalc = fct.getFunction();
		List<Integer> pointslist = new ArrayList<>();
		
		double current = canvas.getIntervall()[0];
		int[] transfPoint = transformer.convertXY(current, toCalc.apply(current));
		Collections.addAll(pointslist, transfPoint[0], transfPoint[1]);
		double nextStep = min_step;  
		
		while(current < canvas.getIntervall()[1]) {	
			nextStep = calculateByPruning(toCalc, current, nextStep, max_step);  
			current = current + nextStep; 
			
			transfPoint = transformer.convertXY(current, toCalc.apply(current));
			Collections.addAll(pointslist, transfPoint[0], transfPoint[1]);
		}
		int[] polygon = new int[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			polygon[i] = pointslist.get(i);
		return polygon;
		
	}
	

	protected double calculateByPruning(Function<Double, Double> function, double x, double lastStep, double max_step) {
		double nextStep = lastStep;
		double error;

		for (int i = 0; i < MAX_ITERATIONS; i++) {
			error = calculateError(function, x, nextStep);
			
			if (Math.abs(error) >= error) {
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

		return error;
	}
}