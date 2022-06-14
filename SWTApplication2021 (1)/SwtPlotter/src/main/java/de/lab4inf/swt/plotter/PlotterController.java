package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class PlotterController implements PropertyChangeListener {

	PlotterModel modell;
	PlotterView view;
	int lineStyle = 0;
	int[] color;
	Random rdm;

	static NewParser jsEngine = new NewParser();

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

			String script = evt.getNewValue().toString();

			HashMap<String, PlotterFunction> myMap = jsEngine.parser(script);

			if (myMap.keySet().contains("Dummy")) {
				view.setMyText("Ungültige Eingabe");
				view.getMyText().setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				return;
			} else {
				for (Map.Entry<String, PlotterFunction> entry : myMap.entrySet()) {
					PlotterFunction fct = entry.getValue();
					if (color == null)
						color = new int[] { rdm.nextInt(255), rdm.nextInt(255), rdm.nextInt(255) };
					fct.setColor(color);
					fct.setLineStyle(this.lineStyle+1);
					modell.addFunction(entry.getKey(), fct); // f(x);PlotterFct
					this.color = null;
				}
			}
			view.getMyText().setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		}

		else if (evt.getPropertyName() == "clear") {
			modell.clear();
		}

		else if (evt.getPropertyName() == "removeFunctions") {
			String toBeRemoved = evt.getNewValue().toString();
			modell.removeFunctions(toBeRemoved);
		}

		else if (evt.getPropertyName() == "styleLine") {
			this.lineStyle = (int) evt.getNewValue();
		}

		else if (evt.getPropertyName() == "color") {
			RGB mycolor = (RGB) evt.getNewValue();
			this.color = new int[] { mycolor.red, mycolor.green, mycolor.blue };
//			this.color[0] = ;
//			this.color[1] = mycolor.green;
//			this.color[2] = mycolor.blue;
		}

	}

}
