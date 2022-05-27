package de.lab4inf.swt.WidthStrategy;

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
		Trafo transformer = new Trafo(canvas);
		Function<Double, Double> toCalc = fct.getFunction();
		//error = ((maxX-minX) / (double) width);
		error = 1 / ((double) width);

		xy = new TreeMap<Double, Double>();
		xy.put(minX, toCalc.apply(minX));
		xy.put(maxX, toCalc.apply(maxX));
		
		double mid = (minX + maxX) / 2;
		xy.put(mid, toCalc.apply(mid));
		addPoint(toCalc, minX, mid, false);
		addPoint(toCalc, mid, maxX, false);

		int[] xyArray = new int[xy.size() * 2];
		int i = 0;
		int[] point;
		for (Map.Entry<Double, Double> entry : xy.entrySet()) {
			point = transformer.convertXY(entry.getKey(), entry.getValue());
			xyArray[i] = point[0];
			xyArray[i + 1] = point[1];
			i += 2;
		}
		return xyArray;
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