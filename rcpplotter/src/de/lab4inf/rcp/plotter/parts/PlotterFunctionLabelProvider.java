package de.lab4inf.rcp.plotter.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import de.lab4inf.swt.plotter.PlotterFunction;

public class PlotterFunctionLabelProvider extends ColumnLabelProvider {


	@Override
	public String getText(Object element) {
		if(element instanceof int[]) {
			int[] color  = (int [])element;
			return "Color {" + String.valueOf(color[0]) + ", " + String.valueOf(color[1]) + ", " + String.valueOf(color[2]) + "}";
		}
		else if (element instanceof PlotterFunction)
			return ((PlotterFunction) element).getName().split("=")[0]; 
		else if(element instanceof Integer) {
			Integer val = (Integer) element;
			switch (val) {
				case 1:
					return new String("Solid");
				case 2:
					return new String("Dash");
				case 3:
					return new String("Dot");
				case 4:
					return new String("DashDotDot");
				case 5:
					return new String("DashDot");
				default:
					break;
			}
		}
		return element.toString();
	}
//	@Override
//	public void update(ViewerCell cell) {
//		Object element = cell.getElement();
//		
//		
//		cell.setText(getText(element));
//		
//	}
}
