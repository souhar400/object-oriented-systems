package de.lab4inf.swt.WidthStrategy;


import java.util.function.Function;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public interface WidthStrategy {

	public int[] calculatePoints(SWTCanvasPlotter canvas, Function<Double, Double> fct);
}
