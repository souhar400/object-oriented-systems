package de.lab4inf.swt.WidthStrategy;


import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public interface StepSizeStrategy {

	public int[] calculatePoints(SWTCanvasPlotter canvas, PlotterFunction fct);
}
