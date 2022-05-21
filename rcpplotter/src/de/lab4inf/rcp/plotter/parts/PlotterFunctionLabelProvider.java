package de.lab4inf.rcp.plotter.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class PlotterFunctionLabelProvider extends LabelProvider {


	@Override
	public String getText(Object element) {
		if(element instanceof int[]) {
			int[] color  = (int [])element;
			Color c = new Color(color[0], color[1], color[2]);
			return c.toString();
		}
		
		return element.toString();
	}

}
