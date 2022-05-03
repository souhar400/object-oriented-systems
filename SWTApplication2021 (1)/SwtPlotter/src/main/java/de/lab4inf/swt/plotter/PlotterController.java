package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.eclipse.swt.graphics.RGB;



public class PlotterController implements PropertyChangeListener {

	PlotterModel modell;
	PlotterView view;
	int lineStyle=0; 
	int[] color ; 
	Random rdm ;  

	static JSEngine jsEngine = new JSEngine();
	
	public PlotterController(PlotterModel modell, PlotterView view) {
		this.modell = modell;
		this.view = view;
		this.rdm = new Random(); 
		this.color = null;
	}

	public void activateView() {
		view.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "functionScript") {
			//myFct.setColor(this.color);
			
			
			
			String script = evt.getNewValue().toString();
			Map.Entry<String, PlotterFunction> result= jsEngine.parser(script,  this.color,  modell.getFunctions());
			
			PlotterFunction myFct = result.getValue();
			myFct.setLineStyle(this.lineStyle+1);
			modell.addFunction(result.getKey(), myFct);
			
			this.color=null;
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
