package de.lab4inf.rcp.plotter.parts;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import de.lab4inf.swt.WidthStrategy.*;
import de.lab4inf.swt.plotter.JSEngine;
import de.lab4inf.swt.plotter.NewParser;
import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public class WorkbenchPropertyChangeListener implements IPropertyChangeListener {
	private SWTCanvasPlotter plotter;
	private ModelProvider model;
	private static NewParser jsEngine = new NewParser();
	private int lineStyle;
	private int[] color;
	private Random rdm;

	public WorkbenchPropertyChangeListener(ModelProvider model) {
		this.model = model;
		this.rdm = new Random();
		this.color = null;
		this.lineStyle = 0;
	}

	public void registerCanvas(SWTCanvasPlotter plotter) {
		this.plotter = plotter;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty() == null)
			return;
		String prop = event.getProperty();
		String newValue = (String) event.getNewValue();
		if (prop.equals("removeFunction")) {
			model.removeFunctions(newValue);
		} else if (prop.equals("addFunction")) {
			HashMap<String, PlotterFunction> myMap = jsEngine.parser(newValue);
			if (myMap.keySet().contains("Dummy"))
				System.out.println("kleine Ausgabe...");
			else {
				for (Map.Entry<String, PlotterFunction> entry : myMap.entrySet()) {
					PlotterFunction fct = entry.getValue();
					if (color == null)
						color = new int[] { rdm.nextInt(255), rdm.nextInt(255), rdm.nextInt(255) };
					fct.setColor(color);
					fct.setLineStyle(this.lineStyle + 1);
					model.addFunction(entry.getKey(), fct); // f(x);PlotterFct
					this.color = null;
				}
			}
		} else if (prop.equals("clear"))
			model.clear();
		else if (prop.equals("strategy")) {
			StepSizeStrategy strat = null;
			if (newValue.equals("Curvature"))
				strat = new CurvatureStepSizeStrategy();
			if (newValue.equals("Pruning"))
				strat = new PrunningStepSizeStrategy();
			if (newValue.equals("Error"))
				strat = new ErrorStepSizeStrategy();
			if (newValue.equals("DivideAndConquer"))
				strat = new DivideAndConquerStepSizeStrategy();
			if (newValue.equals("Constant"))
				strat = new ConstantStepSizeStrategy();
			if (strat != null)
				plotter.setStrategy(strat);
			plotter.redraw();
		} else if (prop.equals("color")) {
			String[] splitValues = newValue.split(" ");
			int index = 0;
			color = new int[splitValues.length];
			for (String string : splitValues) {
				color[index] = Integer.valueOf(string);
				index++;
			}
			if (event.getOldValue() != null) {
				String key = ((String) event.getOldValue()).split("=")[0];
				PlotterFunction fct = model.getFunctions().get(key);
				fct.setColor(color);
			}
		} else if (prop.equals("styleLine")) {
			this.lineStyle = Integer.valueOf(newValue);
		}

	}

}
