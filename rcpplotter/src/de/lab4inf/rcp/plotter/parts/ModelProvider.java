package de.lab4inf.rcp.plotter.parts;

import java.util.HashMap;

import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;

import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.PlotterModel;

public class ModelProvider extends PlotterModel implements ITreeContentProvider {
	private static ModelProvider instance;
	private Viewer viewer;
	private ModelProvider() {
		super();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object NewInput) {
		this.viewer = viewer;
		viewer.refresh();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		ModelProvider myModel = (ModelProvider) inputElement;
		return myModel.getFunctions().values().toArray();
	}

	public static ModelProvider getInstance() {
		if (instance == null)
			instance = new ModelProvider();

		return instance;

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		PlotterFunction pf = (PlotterFunction) parentElement;
		Object[] rv = { pf.getName(), pf.getColor(), pf.getLineStyle() };
		return (Object[]) rv;
	}

	@Override
	public Object getParent(Object element) {
		TreeSelection selection = (TreeSelection) viewer.getSelection();
		TreePath[] path = (TreePath[])selection.getPathsFor(element);
		if( path.length != 0)
			return path[0].getFirstSegment();
		else return element;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof PlotterFunction) {
			PlotterFunction pf = (PlotterFunction) element;
			if (this.getFunctions().containsValue(pf))
				return true;
		}
		return false;
	}
	public void refresh() {
		this.getSupport().firePropertyChange("redraw", null, this.getFunctions());
	}
}
