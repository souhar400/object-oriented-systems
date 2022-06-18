package de.lab4inf.rcp.plotter.parts;



import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.jface.viewers.TreeViewer;

import org.eclipse.swt.graphics.RGB;


import de.lab4inf.swt.plotter.PlotterFunction;

public class TreeEditingSupport extends EditingSupport {
	private TextCellEditor textEditor;
	public TextCellEditor getTextEditor() {
		return textEditor;
	}

	public void setTextEditor(TextCellEditor textEditor) {
		this.textEditor = textEditor;
	}

	private ColorCellEditor colorEditor;
	private ComboBoxCellEditor comboEditor;

	public TreeEditingSupport(TreeViewer treeViewer) {
		super(treeViewer);
		textEditor = new TextCellEditor(treeViewer.getTree());
		colorEditor = new ColorCellEditor(treeViewer.getTree());
		comboEditor = new ComboBoxCellEditor(treeViewer.getTree(),
				new String[] { "Solid", "Dash", "Dot", "DashDotDot", "DashDot" });
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if (element instanceof String)
			return textEditor;
		if (element instanceof int[])
			return colorEditor;
		return comboEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof int[]) {
			int[] val = (int[]) element;
			return new RGB(val[0], val[1], val[2]);
		}

		return element;
	}

	@Override
	protected void setValue(Object element, Object value) {
		String val = value.toString();
		if (element instanceof int[]) {
			val = val.replaceAll("[^\\d,]", "");
			String[] parts = val.split(",");
			int[] color = (int[]) element;
			int index = 0;
			for (String string : parts) {
				color[index] = Integer.valueOf(string);
				index++;
			}
		}
		if (element instanceof String) {
			TreeViewer treeViewer = (TreeViewer) this.getViewer();
			ModelProvider provider = (ModelProvider) treeViewer.getContentProvider();
			PlotterFunction parent = (PlotterFunction) provider.getParent(element);
			parent.setName(val);
			textEditor.setValue(value);
		}

		if (element instanceof Integer) {
			TreeViewer treeViewer = (TreeViewer) this.getViewer();
			ModelProvider provider = (ModelProvider) treeViewer.getContentProvider();
			Object p = provider.getParent(element);
			if (p instanceof PlotterFunction) {
				PlotterFunction parent = (PlotterFunction) provider.getParent(element);
				int lineStyle = (int) value;
				parent.setLineStyle(++lineStyle);
			}
			provider.refresh();
		}
		this.getViewer().update(element, null);
	}

}
