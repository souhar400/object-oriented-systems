package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.function.Function;



public class PlotterController implements PropertyChangeListener {

	PlotterModel modell;
	PlotterView view;
	int lineStyle=0; 
	int color=0; 

	static JSEngine jsEngine = new JSEngine();
	
	public PlotterController(PlotterModel modell, PlotterView view) {
		this.modell = modell;
		this.view = view;
	}

	public void activateView() {
		view.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "functionScript") {
			String script = evt.getNewValue().toString();
			Map.Entry<String, Function<Double, Double>> result= jsEngine.parser(script);
			PlotterFunction myFct = new PlotterFunction(result.getValue(), result.getKey(), 9, this.lineStyle+1);
			modell.addFunction(result.getKey(), myFct);
			}
		
		else if(evt.getPropertyName() == "clear") {
			modell.clear();
		}
		
		else if(evt.getPropertyName() ==  "removeFunctions") {
			String toBeRemoved = evt.getNewValue().toString(); 
			modell.removeFunctions(toBeRemoved);
		}
		
		
		else if(evt.getPropertyName() ==  "styleLine") {
			this.lineStyle= (int) evt.getNewValue(); 
		}

	}


}
