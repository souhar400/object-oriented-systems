package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.lab4inf.swt.SWTApplication;

public class PlotterView extends SWTApplication implements PropertyChangeListener {
	SWTCanvasPlotter canvas;

	PlotterModel modell;
	Label statusField;
	List functionList;
	String functionScript = "";
	int[] selections = null;
	String removedFunction = null;

	private PropertyChangeSupport support;

	HashMap<String, Function<Double, Double>> functions;

	public PlotterView(PlotterModel modell) {
		super();
		support = new PropertyChangeSupport(this);
		this.modell = modell;
	}

	public void setStatusField(Label statusField) {
		this.statusField = statusField;
	}

	private Label getStatusField() {
		return this.statusField;
	}

	public void setFunctionList(List functions) {
		this.functionList = functions;
	}

	public void setCanvas(SWTCanvasPlotter myCanvas) {
		this.canvas = myCanvas;
	}

	public SWTCanvasPlotter getCanvas() {
		return this.canvas;
	}

	// ToolBar
	protected ToolBar createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.BORDER | SWT.HORIZONTAL);
		ToolItem exitItem = new ToolItem(toolbar, SWT.NONE);
		exitItem.setText("Exit");

		exitItem.addListener(SWT.Selection, (evt) -> setVisible(false));
		return toolbar;
	}

	// StatusBar
	protected Composite createStatusBar(Composite parent) {
		GridData gd;
		Group sb = new Group(parent, SWT.NONE);
		sb.setText("StatusBar");
		sb.setLayout(new GridLayout(1, false));

		Label statusLabel = new Label(sb, SWT.NONE);
		statusLabel.setText("Status: ");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		statusLabel.setLayoutData(gd);

		Label statusContentLabel = new Label(sb, SWT.NONE);
		statusContentLabel.setText("Daten aus dem Controller \t \t \t");
		gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		statusContentLabel.setLayoutData(gd);
		setStatusField(statusContentLabel);
		return sb;
	}

	// Content Area = PlotCanvas-Group
	protected Composite createContentArea(Composite parent) {
		GridData gd;
		Group area = new Group(parent, SWT.NONE);
		area.setLayout(new GridLayout(4, true));
		area.setText("PlotCanvas");
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		area.setLayoutData(gd);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalSpan = 3;

		SWTCanvasPlotter myCanvas = new SWTCanvasPlotter(area, SWT.None);
		setCanvas(myCanvas);
		myCanvas.setLayoutData(gridData);

		Group editMySet = new Group(area, SWT.BORDER);
		editMySet.setLayout(new GridLayout(1, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		editMySet.setLayoutData(gd);

		List functionsList = new List(editMySet, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		setFunctionList(functionsList);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		functionsList.setLayoutData(gd);

		Group addRemoveClean = new Group(editMySet, SWT.BORDER);
		addRemoveClean.setLayout(new GridLayout(4, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		addRemoveClean.setLayoutData(gd);

		Text myText = new Text(addRemoveClean, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		myText.setLayoutData(gd);

		Button addButton = new Button(addRemoveClean, SWT.PUSH);
		addButton.setText("Add");

		Button clearButton = new Button(addRemoveClean, SWT.PUSH);
		clearButton.setText("Clear");

		Button removeButton = new Button(addRemoveClean, SWT.PUSH);
		removeButton.setLayoutData(gridData);
		removeButton.setText("Remove");

		addListeners();

		clearButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection) {
					support.firePropertyChange("clear", 0, 1);
				}
			}
		});

		functionsList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = functionsList.getSelectionIndex();
				if (index == -1)
					removedFunction = null;
				else {
					removedFunction = functionsList.getItem(index);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				int[] selections = functionsList.getSelectionIndices();
				// String[] outText = new String[functionsList.getSelectionCount()];
				// for (int i = 0; i < selections.length; i++) {
				// outText[i] = functionsList.getItem(i);
				// System.out.println(outText[i]);
			}
		});

		removeButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (removedFunction != null)
					support.firePropertyChange("removeFunctions", null, removedFunction);
			}
		});

		addButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection) {
					support.firePropertyChange("functionScript", "", myText.getText());
				}
			}
		});

		return area;

	}

	protected void addListeners() {
		canvas.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				int u = e.x;
				int v = e.y;
				double x = canvas.convertUV(u, v)[0];
				double y = canvas.convertUV(u, v)[1];
				getStatusField().setText(String.format("(u=%04d,v=%03d);(x=%.2f,y=%.2f)", u, v, x, y));
			}
		});

	}

	void setFunctions(HashMap<String, Function<Double, Double>> functions) {
		this.functions = functions;
	}

	HashMap<String, Function<Double, Double>> getFunctions() {
		return this.functions;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName() == "AddFunction") {

			functions = modell.getFunctions();
			this.functionList.removeAll();
			for (String a : functions.keySet())
				this.functionList.add(a);
			updateCanvas(functions);
		} else if (evt.getPropertyName() == "clearFunctions") {
			functions = modell.getFunctions();
			this.functionList.removeAll();
			updateCanvas(functions);
		}

		else if (evt.getPropertyName() == "removeFunction") {
			functions = modell.getFunctions();
			this.functionList.removeAll();
			for (String a : functions.keySet())
				this.functionList.add(a);
			updateCanvas(functions);
		}

	}

	// // to add an Observer to this support observable
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	// // to remove an Observer from support observable
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	public void updateCanvas(HashMap<String, Function<Double, Double>> fctSet) {
		canvas.setFcts(fctSet);
		canvas.redraw();
	}
}
