package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

public class PlotterModel {

	private HashMap<String, Function<Double, Double>> myFunctions;
	private PropertyChangeSupport support;

	public PlotterModel() {
		this.support = new PropertyChangeSupport(this);
		myFunctions = new HashMap<String, Function<Double, Double>>();
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
	protected HashMap<String, Function<Double, Double>> getFunctions() {
		return myFunctions;
	}

	protected void addFunction(String script, Function<Double, Double> myFunction) {
		Objects.requireNonNull(myFunction);
		HashMap<String, Function<Double, Double>> oldMap = this.myFunctions;
		HashMap<String, Function<Double, Double>> newMap = new HashMap<String, Function<Double, Double>>();
		newMap = (HashMap<String, Function<Double, Double>>) oldMap.clone();
		newMap.put(script, myFunction);
		myFunctions = newMap;
		support.firePropertyChange("AddFunction", oldMap, newMap);
	}

	protected void removeFunctions(String myFunctionString) {
		Objects.requireNonNull(myFunctionString);
		HashMap<String, Function<Double, Double>> oldMap = this.myFunctions;
		HashMap<String, Function<Double, Double>> newMap = new HashMap<String, Function<Double, Double>>();
		newMap = (HashMap<String, Function<Double, Double>>) oldMap.clone();
		newMap.remove(myFunctionString);
		myFunctions = newMap;
		support.firePropertyChange("removeFunction", oldMap, newMap);
	}

	protected void clear() {
		HashMap<String, Function<Double, Double>> oldSet = this.myFunctions;
		HashMap<String, Function<Double, Double>> newSet = new HashMap<String, Function<Double, Double>>();
		myFunctions = newSet;
		support.firePropertyChange("clearFunctions", oldSet, newSet);
	}

}
