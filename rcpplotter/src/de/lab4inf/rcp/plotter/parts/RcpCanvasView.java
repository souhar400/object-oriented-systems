package de.lab4inf.rcp.plotter.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IViewSite;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import de.lab4inf.swt.plotter.PlotterController;
import de.lab4inf.swt.plotter.PlotterModel;
import de.lab4inf.swt.plotter.PlotterView;
import de.lab4inf.swt.plotter.Trafo;

public class RcpCanvasView extends ViewPart{
	private PlotterModel modell; 
	private PlotterView view ; 
	private PlotterController controller; 
	private IStatusLineManager manager;

	
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
		
        
        
        
        //Tabs erstellen 
        final TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
        tabFolder.setLayout(new GridLayout(1, true));
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        ////Plotter tab
        TabItem plotterTab = new TabItem(tabFolder, SWT.NULL); 
        plotterTab.setText("Plotter");
        
        Composite plotterComposite = new Composite(tabFolder, SWT.NONE); 
        plotterTab.setControl(plotterComposite);
        plotterComposite.setLayout(new GridLayout(1, true));
        plotterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        ////Functions tab
        
        TabItem functionTab = new TabItem(tabFolder, SWT.NULL); 
        functionTab.setText("Functions");
        
        Composite functionComposite = new Composite(tabFolder, SWT.NONE); 
        functionTab.setControl(functionComposite);
        functionComposite.setLayout(new GridLayout(1, true));
        functionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        //Plotter Tab füllen
        Composite pt = view.createContentArea(plotterComposite);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        pt.setLayoutData(gd);
        
        //Functions Tab füllen
        Composite ft = view.createEditSet(functionComposite);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        ft.setLayoutData(gd);
        
        
        
		//Status Bar 
		Composite sb = view.createStatusBar(parent); 
		gd= new GridData(SWT.FILL, SWT.NONE, true, false);
        sb.setLayoutData(gd);
                
		modell.addPropertyChangeListener(view);
		view.addPropertyChangeListener(controller);
		addMouseMoveListener();
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		manager = site.getActionBars().getStatusLineManager();
		
	}
	public void addMouseMoveListener() {
		view.getCanvas().addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				Trafo trafo = new Trafo(view.getCanvas());
				int u = e.x;
				int v = e.y;
				double[] xy = trafo.convertUV(u, v);
				if(manager != null)
					manager.setMessage(String.format("Koordinaten: x=%.2f,y=%.2f", xy[0], xy[1]));
			}
			
		});
	}
	@Focus
	public void setFocus() {
		view.getCanvas().setFocus();
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