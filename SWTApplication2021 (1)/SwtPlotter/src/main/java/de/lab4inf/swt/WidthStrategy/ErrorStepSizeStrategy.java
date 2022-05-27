package de.lab4inf.swt.WidthStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;
import de.lab4inf.swt.plotter.Trafo;

public class ErrorStepSizeStrategy implements StepSizeStrategy {
	private double error;
	private TreeMap<Double, Double> xy;

	@Override
	public int[] calculatePoints(SWTCanvasPlotter canvas, PlotterFunction fct) {

		double maxX = canvas.getIntervall()[1];
		double minX = canvas.getIntervall()[0];
		double width = canvas.getMaxU();
		List<Integer> pointslist = new ArrayList<>();
		double hoehe = canvas.getMaxV();

		Trafo transformer = new Trafo(canvas);
		Function<Double, Double> toCalc = fct.getFunction();
		// error = ((maxX-minX) / (double) width);
		error = 1 / ((double) width);

		xy = new TreeMap<Double, Double>();
		xy.put(minX, toCalc.apply(minX));

		xy.put(maxX, toCalc.apply(maxX));

		double mid = (minX + maxX) / 2;
		xy.put(mid, toCalc.apply(mid));
		addPoint(toCalc, minX, mid, false);
		addPoint(toCalc, mid, maxX, false);
		double myY;
		int[] xyArray = new int[xy.size() * 2];
		int[] point=null;

		for (Map.Entry<Double, Double> entry : xy.entrySet()) {
			myY = entry.getValue();
			if (!(Double.isNaN(myY) || (int) (canvas.getYOrigin() - myY) > hoehe
					|| (int) (canvas.getYOrigin() - myY) < -hoehe)) {
				point = transformer.convertXY(entry.getKey(), entry.getValue());
				Collections.addAll(pointslist, point[0], point[1]);
			}
		}
		int[] polygon = new int[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			polygon[i] = pointslist.get(i);
		return polygon;
	}

	private void addPoint(Function<Double, Double> toCalc, double left, double right, boolean last) {
		double mid = (left + right) / 2;
		double midY = toCalc.apply(mid);
		boolean next = true;
		xy.put(mid, midY);

		if ((mid - left) < (error / 3))
			return;
		if (Math.abs(((xy.get(left) + xy.get(right)) / 2) - midY) < error)
			return;

		addPoint(toCalc, left, mid, next);
		addPoint(toCalc, mid, right, next);
		return;
	}
}