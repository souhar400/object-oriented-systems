package de.lab4inf.rcp.plotter.parts;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import de.lab4inf.swt.plotter.PlotterFunction;



public class ListView extends ViewPart{
	Tree tree;
	TreeViewer treeViewer;
	ModelProvider provider;

	@PostConstruct
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.NONE);
		treeViewer = new TreeViewer(tree);

		provider = ModelProvider.getInstance();
		PlotterFunctionLabelProvider labelprovider = new PlotterFunctionLabelProvider();
		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(labelprovider);
		addListeners();
	}	

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
	private void addListeners() {
		provider.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				treeViewer.setInput(arg0.getNewValue());
			}});
		
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
