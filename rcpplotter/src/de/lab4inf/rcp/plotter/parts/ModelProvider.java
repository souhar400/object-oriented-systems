package de.lab4inf.rcp.plotter.parts;





import java.util.HashMap;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.PlotterModel;



public class ModelProvider extends PlotterModel implements ITreeContentProvider {
	private static ModelProvider instance;

	
	private ModelProvider() {
		super();
		}
		

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object NewInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((HashMap<String,PlotterFunction>)inputElement).keySet().toArray();
	}
	public static ModelProvider getInstance() {
		if(instance == null)
			instance = new ModelProvider();
		
		return instance;
		
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		PlotterFunction pf = this.getFunctions().get((String) parentElement);
		Object[] rv = {pf.getName().split("=")[1], pf.getColor(), pf.getLineStyle()};
		return (Object[])rv;
	}

	@Override
	public Object getParent(Object element) {
		return (Object)this;
	}

	@Override
	public boolean hasChildren(Object element) {
	if(element instanceof String && this.getFunctions().get((String)element) != null)	
		return true;
	return false;
	}

}
