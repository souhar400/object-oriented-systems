package de.lab4inf.swt.plotter;
import de.lab4inf.swt.SWTApplication;

public class PlotterApplication extends SWTApplication {

	public static void main(String[] args) {
		PlotterModel modell = new PlotterModel();
		PlotterView view = new PlotterView(modell);
		
		modell.addPropertyChangeListener(view);

		
		PlotterController controller = new PlotterController(modell, view);
		view.addPropertyChangeListener(controller);
		controller.activateView();
	}
}
