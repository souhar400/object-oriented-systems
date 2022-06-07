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
	private double uScal; 
	@Override
	public double[] calculatePoints(PlotterFunction fct, double xMin, double xMax, double yMin, double yMax, int width,
			int hoehe) {

//		double maxX = canvas.getIntervall()[1];
//		double minX = canvas.getIntervall()[0];
//		double width = canvas.getMaxU();
		List<Double> pointslist = new ArrayList<>();
		uScal = width / (xMax-xMin);		
//		double hoehe = canvas.getMaxV();

//		Trafo transformer = new Trafo(canvas);
		Function<Double, Double> toCalc = fct.getFunction();
		// error = ((maxX-minX) / (double) width);
		
		error = uScal/width;
//		error = 1 / ((double) width);

		
		
		double vScal = (yMax-yMin)/hoehe; 	
//		double delta = (xMin - xMax)/(hoehe); 
//		double delta =  1 / ((double) hoehe);
		double delta = vScal/(yMax-yMin); 
		
		error= delta; 
		
		xy = new TreeMap<Double, Double>();
		xy.put(xMin, toCalc.apply(xMin));
		xy.put(xMax, toCalc.apply(xMax));

		double mid = (xMin + xMax) / 2;
		xy.put(mid, toCalc.apply(mid));
		addPoint(toCalc, xMin, mid);
		addPoint(toCalc, mid, xMax);

		double myY;
		int[] xyArray = new int[xy.size() * 2];
		int[] point = null;

		for (Map.Entry<Double, Double> entry : xy.entrySet()) {
			pointslist.add(entry.getKey());
			pointslist.add(entry.getValue());
		}
		double[] polygon = new double[pointslist.size()];
		for (int i = 0; i < pointslist.size(); i++)
			polygon[i] = pointslist.get(i);
		return polygon;
	}

	private void addPoint(Function<Double, Double> toCalc, double left, double right) {
		double mid = (left + right) / 2;
		double midY = toCalc.apply(mid);
		xy.put(mid, midY);
		//
		if ((mid - left) < (error / 3))
			return;
		//
		if (Math.abs(((xy.get(left) + xy.get(right)) / 2) - midY) < error)
			return;

		addPoint(toCalc, left, mid);
		addPoint(toCalc, mid, right);
		return;
	}
}