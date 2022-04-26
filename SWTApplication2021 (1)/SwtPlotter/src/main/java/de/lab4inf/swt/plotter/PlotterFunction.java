package de.lab4inf.swt.plotter;

import java.util.function.Function;

public class PlotterFunction {
	public Function<Double, Double> function; 
	public String name; 
	public int color; 
	public int lineStyle; 
	
	public PlotterFunction( Function<Double, Double> function, String name, int color, int lineStyle) {
		this.function=function; 
		this.name=name; 
		this.color=color; 
		this.lineStyle=lineStyle; 
	}
	

}
