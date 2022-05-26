package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class PlotterModel {

	private HashMap<String, PlotterFunction> plotterFunctions;
	private PropertyChangeSupport support;

	public PlotterModel() {
		this.support = new PropertyChangeSupport(this);
		plotterFunctions = new HashMap<String, PlotterFunction>();
	}

	// // to add an Observer to this support observable
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	// // to remove an Observer from support observable
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	////////////
	protected HashMap<String, PlotterFunction> getFunctions() {
		return plotterFunctions;
	}

	protected void addFunction(String script, PlotterFunction myFunction) {
		Objects.requireNonNull(myFunction);
		HashMap<String, PlotterFunction> oldMap = this.plotterFunctions;
		HashMap<String, PlotterFunction> newMap = new HashMap<String, PlotterFunction>();
		newMap = (HashMap<String, PlotterFunction>) oldMap.clone();

		newMap.put(script, myFunction);
		plotterFunctions = newMap;
		support.firePropertyChange("AddFunction", oldMap, newMap);
	}

	protected void removeFunctions(String myFunctionString) {
		Objects.requireNonNull(myFunctionString);
		HashMap<String, PlotterFunction> oldMap = this.plotterFunctions;
		HashMap<String, PlotterFunction> newMap = new HashMap<String, PlotterFunction>();
		newMap = (HashMap<String, PlotterFunction>) oldMap.clone();
		myFunctionString.trim();
		String parts[] = myFunctionString.split("=");
		newMap.remove(parts[0]);
		plotterFunctions = newMap;
		support.firePropertyChange("removeFunction", null, newMap);
	}

	protected void clear() {
		HashMap<String, PlotterFunction> oldSet = this.plotterFunctions;
		HashMap<String, PlotterFunction> newSet = new HashMap<String, PlotterFunction>();
		plotterFunctions = newSet;
		support.firePropertyChange("clearFunctions", oldSet, newSet);
	}

}
