package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.swt.graphics.RGB;



public class PlotterController implements PropertyChangeListener {

	PlotterModel modell;
	PlotterView view;
	int lineStyle=0; 
	int[] color = new int[] {0,0,0};

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
			Map.Entry<String, PlotterFunction> result= jsEngine.parser(script, modell.getFunctions());
			
			PlotterFunction myFct = result.getValue();
			myFct.setColor(this.color);
			myFct.setLineStyle(this.lineStyle+1);
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
		
		else if(evt.getPropertyName() ==  "color") {
			RGB mycolor = (RGB) evt.getNewValue(); 
			this.color = new int[] {mycolor.red,mycolor.green, mycolor.blue}; 
//			this.color[0] = ;
//			this.color[1] = mycolor.green;
//			this.color[2] = mycolor.blue;
		}

	}


}
