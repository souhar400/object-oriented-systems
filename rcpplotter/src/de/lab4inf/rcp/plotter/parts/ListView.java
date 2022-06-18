package de.lab4inf.rcp.plotter.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;


import org.eclipse.jface.viewers.ICellEditorListener;

import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Tree;

import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.ui.part.ViewPart;




public class ListView extends ViewPart {
	
	private TreeViewer treeViewer;
	private ModelProvider provider;


	@PostConstruct
	public void createPartControl(Composite parent) {
		createEditableTree(parent);
		getViewSite().setSelectionProvider(treeViewer);
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
	protected void createEditableTree(Composite parent) {
		provider = ModelProvider.getInstance();
		Tree tree = new Tree(parent, SWT.NONE);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(provider);
		treeViewer.setUseHashlookup(true);

		
		TreeEditingSupport editingSupport = new TreeEditingSupport(treeViewer);
		TextCellEditor textEditor = editingSupport.getTextEditor();
        textEditor.addListener(new ICellEditorListener() {
			
			@Override
			public void editorValueChanged(boolean oldValidState, boolean newValidState) {}
			
			@Override
			public void cancelEditor() {}
			
			@Override
			public void applyEditorValue() {
				String val = (String)textEditor.getValue();
				firePartPropertyChanged("addFunction", null, val);
			}
		});
		
		PlotterFunctionLabelProvider labelprovider = new PlotterFunctionLabelProvider();
		
		TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.NONE);
		column.getColumn().setWidth(300);
		column.getColumn().setText("Funktionen");
		column.setLabelProvider(labelprovider);
		column.setEditingSupport(editingSupport);
		
		treeViewer.setInput(provider);
		treeViewer.refresh();
	}

}
