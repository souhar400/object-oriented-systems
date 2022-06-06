package de.lab4inf.rcp.plotter.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import de.lab4inf.swt.plotter.PlotterFunction;

public class PlotterFunctionLabelProvider extends LabelProvider {


	@Override
	public String getText(Object element) {
		if(element instanceof int[]) {
			int[] color  = (int [])element;
			return "Color {" + String.valueOf(color[0]) + ", " + String.valueOf(color[1]) + ", " + String.valueOf(color[2]) + "}";
		}
		else if (element instanceof PlotterFunction)
			return ((PlotterFunction) element).getName().split("=")[0]; 
		
		return element.toString();
	}

}
