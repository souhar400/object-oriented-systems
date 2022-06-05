package de.lab4inf.rcp.plotter.parts;

import java.util.Map;
import java.util.Random;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import de.lab4inf.swt.WidthStrategy.*;
import de.lab4inf.swt.plotter.JSEngine;
import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public class WorkbenchPropertyChangeListener implements IPropertyChangeListener {
	private SWTCanvasPlotter plotter;
	private ModelProvider model;
	private static JSEngine jsEngine = new JSEngine();
	int lineStyle=0; 
	int[] color ; 
	Random rdm ;  
	
	public WorkbenchPropertyChangeListener(ModelProvider model) {
		this.model = model;
		this.rdm = new Random(); 
		this.color = null;
	}
	public void registerCanvas(SWTCanvasPlotter plotter) {
		this.plotter = plotter;
	}
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty() == null) return;
		String prop = event.getProperty();
		String newValue = (String)event.getNewValue();
		if(prop.equals("removeFunction")) {
			model.removeFunctions(newValue);
		}
		else if(prop.equals("addFunction")) {
			Map.Entry<String, PlotterFunction> retVal = jsEngine.parser(newValue, model.getFunctions());
			PlotterFunction myFct = retVal.getValue();
			if(retVal.getKey().equals("Dummy")) return;
			if(color == null)
					color=  new int[] { rdm.nextInt(255),  rdm.nextInt(255),  rdm.nextInt(255)}; 
			
			myFct.setColor(color);
			myFct.setLineStyle(this.lineStyle+1);
			model.addFunction(retVal.getKey(), myFct);
			this.color=null;
		}
		else if(prop.equals("clear"))
			model.clear();
		if(prop.equals("strategy")) {
			StepSizeStrategy strat = null;
			if(newValue.equals("Curvature"))
				strat = new CurvatureStepSizeStrategy();
			if(newValue.equals("Pruning"))
				strat = new PrunningStepSizeStrategy();
			if(newValue.equals("Error"))
				strat= new ErrorStepSizeStrategy();	
			if(newValue.equals("DivideAndConquer"))
				strat = new DivideAndConquerStepSizeStrategy();
			if(newValue.equals("Constant"))
				strat = new ConstantStepSizeStrategy();
			if(strat != null)
				plotter.setStrategy(strat);
			plotter.redraw();
		}
	}
	

}
