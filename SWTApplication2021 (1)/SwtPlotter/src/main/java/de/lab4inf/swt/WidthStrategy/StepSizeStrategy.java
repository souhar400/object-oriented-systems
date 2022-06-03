package de.lab4inf.swt.WidthStrategy;


import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public interface StepSizeStrategy {

	public double[] calculatePoints(PlotterFunction fct, double xMin, double xMax, double yMin, double yMax, int width, int hoehe);
}