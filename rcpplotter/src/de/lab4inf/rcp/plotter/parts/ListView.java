package de.lab4inf.rcp.plotter.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;



import org.eclipse.jface.viewers.ISelection;

import org.eclipse.jface.viewers.TreeViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;


public class ListView extends ViewPart {
	private Tree tree;
	private TreeViewer treeViewer;
	private ModelProvider provider;
	private RcpCanvasView rcpView;

	@PostConstruct
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.NONE);
		treeViewer = new TreeViewer(tree);
		
		IWorkbenchPage[] pages = this.getViewSite().getWorkbenchWindow().getPages();
		IViewPart part = pages[0].findView("de.lab4inf.rcp.plotter.parts.RcpCanvasView");
		rcpView = (RcpCanvasView) part;
		
		getViewSite().setSelectionProvider(treeViewer);
		
		provider = ModelProvider.getInstance();
		PlotterFunctionLabelProvider labelprovider = new PlotterFunctionLabelProvider();
		
		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(labelprovider);
		treeViewer.setInput(provider);
		treeViewer.refresh();
		
		addListeners();
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	private void addListeners() {

//		treeViewer.addSelectionChangedListener((new ISelectionChangedListener() {
//
//			@Override
//			public void selectionChanged(SelectionChangedEvent event) {
//				if (event.getSelection().isEmpty()) {}
//				if (event.getSelection() instanceof IStructuredSelection) {
//					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
//					if (selection.getFirstElement() instanceof PlotterFunction)
//						for (Iterator<PlotterFunction> iterator = selection.iterator(); iterator.hasNext();) {
//							PlotterFunction fct = iterator.next();
//							rcpView.getView().setRemovedFunction(fct.getName());
//							rcpView.getView().setMyText(fct.getName());
//						}
//				}
//			}
//
//		}));

		provider.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				treeViewer.refresh();
			}
		});

		Tree tree = (Tree) treeViewer.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (item.getItemCount() > 0) {
					item.setExpanded(!item.getExpanded());
					treeViewer.refresh();
				}
			}
		});
	}
}
