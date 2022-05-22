package de.lab4inf.swt.plotter;

import java.util.function.Function;

public class PlotterFunction {
	private Function<Double, Double> function;
	private String name;
	private int[] color;
	private int lineStyle;
	public PlotterFunction() {}
	public PlotterFunction(Function<Double, Double> function, String name, int[] color, int lineStyle) {
		if(function != null)
			this.function = function;
		this.name = name;
		if (color != null)
			this.color = color;
		this.lineStyle = lineStyle;
	}

	public void setColor(int[] color) {
		this.color = color;
	}

	public void setLineStyle(int style) {
		this.lineStyle = style;
	}
	public Function<Double, Double> getFunction() {
		return function;
	}
	public void setFunction(Function<Double, Double> function) {
		this.function = function;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getColor() {
		return color;
	}
	public int getLineStyle() {
		return lineStyle;
	}
    public double derivationOne(double x, double h) {
        return (function.apply(x + h) - function.apply(x - h)) / (2 * h);
    }
    public double derivationTwo(double x, double h) {
        return (function.apply(x + h) + function.apply(x - h) - 2 * function.apply(x)) / Math.pow(h, 2);
    }
    public double curvature(double x, double h) {
        return derivationTwo(x, h) / (Math.pow(1 + Math.pow(derivationOne(x, h), 2), 3. / 2.));
    }

}
