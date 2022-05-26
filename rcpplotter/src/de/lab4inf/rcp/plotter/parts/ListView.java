package de.lab4inf.rcp.plotter.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import de.lab4inf.swt.plotter.PlotterFunction;

public class ListView extends ViewPart {
	Tree tree;
	TreeViewer treeViewer;
	ModelProvider provider;
	RcpCanvasView rcpView;

	@PostConstruct
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.NONE);
		treeViewer = new TreeViewer(tree);

		IWorkbenchPage[] pages = this.getViewSite().getWorkbenchWindow().getPages();
		IViewPart part = pages[0].findView("de.lab4inf.rcp.plotter.parts.RcpCanvasView");
		rcpView = (RcpCanvasView) part;

		provider = ModelProvider.getInstance();
		PlotterFunctionLabelProvider labelprovider = new PlotterFunctionLabelProvider();
		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(labelprovider);
		treeViewer.setInput(provider);

		treeViewer.refresh();
		rcpView.view.setMyViewer(treeViewer);
		addListeners();
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	private void addListeners() {

		treeViewer.addSelectionChangedListener((new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection().isEmpty()) {

				}
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					if (selection.getFirstElement() instanceof PlotterFunction)
						for (Iterator<PlotterFunction> iterator = selection.iterator(); iterator.hasNext();) {
							PlotterFunction fct = iterator.next();
							rcpView.view.setRemovedFunction(fct.getName());
							rcpView.view.setMyText(fct.getName());
						}
					else {
						
					}
				}
			}

		}));

		provider.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// treeViewer.setInput(arg0.getNewValue());
			}
		});

		Tree tree = (Tree) treeViewer.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (item.getItemCount() > 0) {
					item.setExpanded(!item.getExpanded());
					// update the viewer
					treeViewer.refresh();
				}
			}
		});
	}

}
