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

	private static final int MAX_STEP_PX = 50;

	private static final double h = 5;

	@Override
	public int[] calculatePoints(SWTCanvasPlotter canvas, PlotterFunction fct) {
		int sizeScreen = canvas.getMaxU();
		double sizeWorld = canvas.getIntervall()[1];
		Trafo transformer = new Trafo(canvas);

		double min_step = sizeWorld / sizeScreen * MIN_STEP_PX;
		double max_step = sizeWorld / sizeScreen * MAX_STEP_PX;

		Function<Double, Double> toCalc = fct.getFunction();

		List<Integer> pointslist = new ArrayList<>();

		double current = canvas.getIntervall()[0];
		double step = min_step;
		double slope = fct.curvature(current, h);

		int[] point = transformer.convertXY(current, toCalc.apply(current));
		Collections.addAll(pointslist, point[0], point[1]);
		while (current < sizeWorld) {
			while (step < max_step && Math.abs(slope - fct.curvature(current + step, h * step)) < .1)
				step += min_step;

			current += step;
			step = min_step;
			point = transformer.convertXY(current, toCalc.apply(current));
			Collections.addAll(pointslist, point[0], point[1]);
		}
		int[] polygon = new int[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			polygon[i] = pointslist.get(i);
		return polygon;
	}
}
