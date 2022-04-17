package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Function;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class PlotterController implements PropertyChangeListener {

	PlotterModel modell;
	PlotterView view;

	static ScriptEngineManager factory = new ScriptEngineManager();
	static ScriptEngine engine = factory.getEngineByName("nashorn");

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

			Bindings bindings = engine.createBindings();

			String script = evt.getNewValue().toString();
			Function<Double, Double> fct = x -> {
				bindings.put("x", x);
				try {
					return (Double) engine.eval(script, bindings);
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Object bindingsResult = engine.eval(script); }
				return x;// TODO: Throw Exeption that function nicht geparst werden konnte !!!

			};

			modell.addFunction(script, fct);
		}
		
		else if(evt.getPropertyName() == "clear") {
			modell.clear();
		}
		
		else if(evt.getPropertyName() ==  "removeFunctions") {
			modell.removeFunctions((String) evt.getNewValue());
		}

	}


}
