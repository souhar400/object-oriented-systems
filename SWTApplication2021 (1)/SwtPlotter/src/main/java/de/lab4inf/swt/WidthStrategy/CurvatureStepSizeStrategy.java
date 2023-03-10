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

public class CurvatureStepSizeStrategy implements StepSizeStrategy {

	private static final double MIN_STEP_PX = 1;

	private static final int MAX_STEP_PX = 10;

	private static final double h = 5;

	@Override
	public double[] calculatePoints(PlotterFunction fct, double xMin, double xMax, double yMin, double yMax, int width, int hoehe) {
//		int sizeScreen = canvas.getMaxU();
//		double sizeWorld = canvas.getIntervall()[1];
//		Trafo transformer = new Trafo(canvas);
//		double hoehe = canvas.getMaxV();

		double min_step = xMax / width * MIN_STEP_PX;
		double max_step = xMax / width * MAX_STEP_PX;

		Function<Double, Double> toCalc = fct.getFunction();

		List<Double> pointslist = new ArrayList<>();

		double current = xMin;
		double step = min_step;
		double slope = fct.curvature(current, h);
		double myY = toCalc.apply(current); 
		
		pointslist.add(current); 
		pointslist.add(toCalc.apply(current)); 
		while (current < xMax) {
			
			while (step < max_step && Math.abs(slope - fct.curvature(current + step, h * step)) < .1)
				step += min_step;

			current += step;
			step = min_step;
		
			myY = toCalc.apply(current); 
			pointslist.add(current); 
			pointslist.add(toCalc.apply(current)); 
		}
		double[] polygon = new double[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			polygon[i] = pointslist.get(i);
		return polygon;
	}
}