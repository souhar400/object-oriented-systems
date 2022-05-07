package de.lab4inf.rcp.plotter.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.lab4inf.swt.plotter.PlotterController;
import de.lab4inf.swt.plotter.PlotterModel;
import de.lab4inf.swt.plotter.PlotterView;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;

public class RcpCanvasView{
	private PlotterModel modell; 
	private PlotterView view ; 
	private PlotterController controller; 

	@PostConstruct
	public void createPartControl(Composite parent) {
		GridData gd; 
		modell = new PlotterModel(); 
		view = new PlotterView(modell); 
		controller = new PlotterController(modell, view);

		//Parent einstellen
		parent.setLayout(new GridLayout(1, true));
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        parent.setLayoutData(gd);
		
		//Conten Area
        Composite ca = view.createContentArea(parent);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        ca.setLayoutData(gd);
        

		//Status Bar 
		Composite sb = view.createStatusBar(parent); 
		gd= new GridData(SWT.FILL, SWT.NONE, true, false);
        sb.setLayoutData(gd);
      

		modell.addPropertyChangeListener(view);
		view.addPropertyChangeListener(controller);
	}

	@Focus
	public void setFocus() {
		//canvas.setFocus();

	}

	/**
	 * This method is kept for E3 compatiblity. You can remove it if you do not
	 * mix E3 and E4 code. <br/>
	 * With E4 code you will set directly the selection in ESelectionService and
	 * you do not receive a ISelection
	 * 
	 * @param s
	 *            the selection received from JFace (E3 mode)
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection s) {
		if (s==null || s.isEmpty())
			return;

		if (s instanceof IStructuredSelection) {
			IStructuredSelection iss = (IStructuredSelection) s;
			if (iss.size() == 1)
				setSelection(iss.getFirstElement());
			else
				setSelection(iss.toArray());
		}
	}

	/**
	 * This method manages the selection of your current object. In this example
	 * we listen to a single Object (even the ISelection already captured in E3
	 * mode). <br/>
	 * You should change the parameter type of your received Object to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current object received
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {

		// Remove the 2 following lines in pure E4 mode, keep them in mixed mode
		if (o instanceof ISelection) // Already captured
			return;

		// Test if label exists (inject methods are called before PostConstruct)

	}

	/**
	 * This method manages the multiple selection of your current objects. <br/>
	 * You should change the parameter type of your array of Objects to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current array of objects received in case of multiple selection
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object[] selectedObjects) {

		// Test if label exists (inject methods are called before PostConstruct)

	}
}
