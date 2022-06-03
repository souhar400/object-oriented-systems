package de.lab4inf.swt.WidthStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public class ConstantStepSizeStrategy implements StepSizeStrategy {

	@Override
	public double[] calculatePoints(PlotterFunction fct, double xMin, double xMax, double yMin, double yMax, int width, int hoehe) {

		Function<Double, Double> myFct = fct.getFunction();

		List<Double> list = new ArrayList<Double>();
		double schritt = (xMax-xMin)/width; 
		double current= xMin; 
		while(current < xMax) {	
			list.add(current); 
			list.add(myFct.apply(current)); 
			current = current + schritt; 
		}
		
		double[] polygon = new double[list.size()];
		for (int i = 0; i < list.size(); i++)
			polygon[i] = list.get(i);
		return polygon;
	}
	
}