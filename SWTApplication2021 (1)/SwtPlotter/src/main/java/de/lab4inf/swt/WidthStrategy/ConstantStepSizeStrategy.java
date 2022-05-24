package de.lab4inf.swt.WidthStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public class ConstantStepSizeStrategy implements StepSizeStrategy {

	@Override
	public int[] calculatePoints(SWTCanvasPlotter canvas, PlotterFunction fct) {
		double[] xIntervall = canvas.getIntervall();
		double[] yIntervall = canvas.getyIntervall();
		
		int breite = canvas.getMaxU();
		int hoehe = canvas.getMaxV();
		int xOrigin = canvas.getXOrigin();
		
		double scalU = breite / ((xIntervall[1] - xIntervall[0]));
		double scalV = hoehe / (yIntervall[1] - yIntervall[0]);
		Function<Double, Double> myFct = fct.getFunction();

		List<Integer> list = new ArrayList<Integer>();
		for (int k = -xOrigin; k <= breite - xOrigin; k = k + 1) {
			double zwischenK = k * ((xIntervall[1] - xIntervall[0]) / breite);
			double fctValue = myFct.apply(zwischenK);
			int yPixel = (int) (scalV * fctValue);

			if (!(yPixel < -hoehe || yPixel > hoehe || Double.isNaN(fctValue))) {
				list.add(canvas.translateU(zwischenK * scalU));
				list.add(canvas.translateV(scalV * fctValue));
			}
		}
		int[] polygon = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			polygon[i] = list.get(i);

		return polygon;
	}

}
