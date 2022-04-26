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
		String parts[] = script.split("=");
		for(Map.Entry<String, PlotterFunction> entry : newMap.entrySet()) {
			String key = entry.getKey(); 
			if(key.contains(parts[0])) newMap.remove(key);
		}
		newMap.put(script, myFunction);
		plotterFunctions = newMap;
		support.firePropertyChange("AddFunction", oldMap, newMap);
	}

	protected void removeFunctions(String myFunctionString) {
		Objects.requireNonNull(myFunctionString);
		HashMap<String, PlotterFunction> oldMap = this.plotterFunctions;
		HashMap<String, PlotterFunction> newMap = new HashMap<String, PlotterFunction>();
		newMap = (HashMap<String, PlotterFunction>) oldMap.clone();
		newMap.remove(myFunctionString);
		plotterFunctions = newMap;
		support.firePropertyChange("removeFunction", oldMap, newMap);
	}

	protected void clear() {
		HashMap<String, PlotterFunction> oldSet = this.plotterFunctions;
		HashMap<String, PlotterFunction> newSet = new HashMap<String, PlotterFunction>();
		plotterFunctions = newSet;
		support.firePropertyChange("clearFunctions", oldSet, newSet);
	}

}
