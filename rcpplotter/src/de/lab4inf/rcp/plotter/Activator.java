package de.lab4inf.rcp.plotter;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.views.IViewDescriptor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.lab4inf.rcp.plotter.parts.ModelProvider;
import de.lab4inf.rcp.plotter.parts.RcpCanvasView;
import de.lab4inf.rcp.plotter.parts.WorkbenchPropertyChangeListener;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static ModelProvider model;
	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		model = ModelProvider.getInstance();
		ServiceReference<IWorkbench> ref = bundleContext.getServiceReference(IWorkbench.class);
		IWorkbench workbench = bundleContext.getService(ref);
		IViewReference[] viewRefs = workbench.getActiveWorkbenchWindow().getActivePage().getViewReferences();
		WorkbenchPropertyChangeListener wbpcl = new WorkbenchPropertyChangeListener(model);
		for(IViewReference viewRef : viewRefs) {
			if(viewRef.getPart(true) instanceof RcpCanvasView) {
				RcpCanvasView cv = (RcpCanvasView) viewRef.getPart(true);
				wbpcl.registerCanvas(cv.getPlotter());
			}
			viewRef.addPartPropertyListener(wbpcl);
		}
		Activator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	public static ModelProvider getModel() {
		return model;
	}
}
