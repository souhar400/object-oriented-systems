package de.lab4inf.swt.plotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

import org.apache.maven.surefire.shared.compress.archivers.zip.ZipArchiveEntryRequest;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.lab4inf.swt.SWTApplication;
import de.lab4inf.swt.WidthStrategy.ConstantStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.CurvatureStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.DivideAndConquerStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.ErrorStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.PrunningStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.StepSizeStrategy;

public class PlotterView extends SWTApplication implements PropertyChangeListener {
	SWTCanvasPlotter canvas;
	private Text myText;
	private TreeViewer myViewer; 
	private StepSizeStrategy strategy; 

	public StepSizeStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(StepSizeStrategy strategy) {
		this.strategy = strategy;
	}

	public TreeViewer getMyViewer() {
		return myViewer;
	}

	public void setMyViewer(TreeViewer myViewer) {
		this.myViewer = myViewer;
	}

	public Text getMyText() {
		return myText;
	}

	public void setMyText(String myText) {
		this.myText.setText(myText);
	}

	PlotterModel modell;
	Label statusField;
	List functionList;
	String functionScript = "";
	int[] selections = null;
	String removedFunction = null;
	

	private Trafo trafo;
	private PropertyChangeSupport support;

	HashMap<String, PlotterFunction> plotterFunctions;

	public PlotterView(PlotterModel modell) {
		super();
		support = new PropertyChangeSupport(this);
		this.modell = modell;
		this.strategy = new ConstantStepSizeStrategy(); 
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
	
	public String getRemovedFunction() {
		return removedFunction;
	}

	public void setRemovedFunction(String removedFunction) {
		this.removedFunction = removedFunction;
	}

	// ToolBar
	@Override
	protected ToolBar createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.BORDER | SWT.HORIZONTAL);
		ToolItem exitItem = new ToolItem(toolbar, SWT.NONE);
		exitItem.setText("Exit");

		exitItem.addListener(SWT.Selection, (evt) -> setVisible(false));
		return toolbar;
	}

	// StatusBar
	@Override
	protected Composite createParamBar(Composite parent) {
		GridData gd;
		Group sb = new Group(parent, SWT.HORIZONTAL);
		sb.setText("Parameters");
		sb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		sb.setLayout(new GridLayout(10, false));

		Label statusLabel = new Label(sb, SWT.NONE);
		statusLabel.setText("Schritteweite der Units : ");
		Scale steps = new Scale(sb, SWT.BORDER | SWT.HORIZONTAL);
		steps.setMaximum(10);
		steps.setMinimum(1);
		steps.setPageIncrement(1);
		steps.setSelection(1);

		steps.addListener(SWT.Selection, (evt) -> {
			canvas.schrittweite = steps.getSelection();
			updateCanvas(plotterFunctions);
		});
		return sb;
	}

	// StatusBar
	public Composite createStatusBar(Composite parent) {
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
	public Composite createContentArea(Composite parent) {
		GridData gd;
		Group area = new Group(parent, SWT.NONE);
		area.setLayout(new GridLayout(4, true));
		area.setText("PlotCanvas");
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		area.setLayoutData(gd);

		// TODOText myText
		// area.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

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

		Group coorSystem = new Group(editMySet, SWT.BORDER);
		coorSystem.setLayout(new GridLayout(4, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		coorSystem.setLayoutData(gd);
		
		
	
				

		Label xMinLabel = new Label(coorSystem, SWT.NONE);
		xMinLabel.setText("Xmin ");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		xMinLabel.setLayoutData(gd);

		Text myXminText = new Text(coorSystem, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		myXminText.setText(String.valueOf(canvas.getIntervall()[0]));
		myXminText.setLayoutData(gd);

		Label xMaxLabel = new Label(coorSystem, SWT.NONE);
		xMaxLabel.setText("Xmax ");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		xMaxLabel.setLayoutData(gd);

		Text myXmaxText = new Text(coorSystem, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		myXmaxText.setText(String.valueOf(canvas.getIntervall()[1]));
		myXmaxText.setLayoutData(gd);

		Label yMinLabel = new Label(coorSystem, SWT.NONE);
		yMinLabel.setText("Ymin ");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		yMinLabel.setLayoutData(gd);

		Text myYminText = new Text(coorSystem, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		myYminText.setLayoutData(gd);

		Label yMaxLabel = new Label(coorSystem, SWT.NONE);
		yMaxLabel.setText("Ymax ");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		yMaxLabel.setLayoutData(gd);

		Text myYmaxText = new Text(coorSystem, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		myYmaxText.setLayoutData(gd);

		
		
		Label strategyLabel = new Label(editMySet, SWT.NONE);
		strategyLabel.setText("Strategy");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		strategyLabel.setLayoutData(gd);
		
		
		Combo combo = new Combo(editMySet, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		combo.setLayoutData(gd);
		combo.setItems(new String[] { "Curvature", "Pruninng", "Error rate", "Divide and conquer", "Constant step size"});
		combo.select(0);
		
		
		Button updateButton = new Button(editMySet, SWT.PUSH);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 4;
		updateButton.setLayoutData(gd);
		updateButton.setText("Update");
		
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(combo.getSelectionIndex()) {
				case 0:
					setStrategy(new CurvatureStepSizeStrategy());
					break; 
				case 1:
					setStrategy(new PrunningStepSizeStrategy());
					break; 
				case 2:
					setStrategy(new ErrorStepSizeStrategy());
					break; 
				case 3:
					setStrategy(new DivideAndConquerStepSizeStrategy());
					break; 
				case 4: 
					setStrategy(new ConstantStepSizeStrategy());
					break; 
				}
				canvas.setStrategy(strategy);
				canvas.redraw();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
	
		});
		
		
		updateButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection) {
					if (!myXminText.getText().isBlank() && !myXmaxText.getText().isBlank()) {
						double myXmax = Double.valueOf(myXmaxText.getText());
						double myXmin = Double.valueOf(myXminText.getText()); 
						canvas.setDrawIntervall(myXmin,myXmax);
						canvas.setOrigin((int) (canvas.getMaxU()*(1-(myXmax/(myXmax-myXmin)))),canvas.getYOrigin()); 
					}

					if (!myYminText.getText().isBlank() && !myYminText.getText().isBlank()) {
						
						double myYmax = Double.valueOf(myYmaxText.getText());
						double myYmin = Double.valueOf(myYminText.getText()); 	
						canvas.setyIntervall(myYmin, myYmax);
						canvas.setOrigin(canvas.getXOrigin(),(int) (canvas.getMaxV()*((myYmax/(myYmax-myYmin)))-1)); 
					}
					canvas.redraw();

				}
			}
		});
		
		
		
		createEditSet(editMySet); 
		
		addListeners();
		
		
		return area;

	}
	
	public Composite createEditSet(Composite parent) {
		GridData gd;


		Group editMySet = new Group(parent, SWT.BORDER);
		editMySet.setLayout(new GridLayout(1, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		editMySet.setLayoutData(gd);

		List functionsList = new List(editMySet, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		setFunctionList(functionsList);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		functionsList.setLayoutData(gd);

		Group addRemoveClean = new Group(editMySet, SWT.BORDER);
		addRemoveClean.setLayout(new GridLayout(6, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		addRemoveClean.setLayoutData(gd);

		myText = new Text(addRemoveClean, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 4;
		myText.setLayoutData(gd);

		Combo combo = new Combo(addRemoveClean, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(SWT.RIGHT, SWT.FILL, false, false);
		combo.setLayoutData(gd);
		combo.setItems(new String[] { "Solid", "Dash", "Dot", "DashDotDot", "DashDot" });
		combo.select(0);

		ColorSelector cs = new ColorSelector(addRemoveClean);
		cs.setEnabled(true);

		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;

		Button addButton = new Button(addRemoveClean, SWT.PUSH);
		addButton.setLayoutData(gd);
		addButton.setText("Add");

		Button clearButton = new Button(addRemoveClean, SWT.PUSH);
		clearButton.setLayoutData(gd);
		clearButton.setText("Clear");

		Button removeButton = new Button(addRemoveClean, SWT.PUSH);
		removeButton.setLayoutData(gd);
		removeButton.setText("Remove");

		

		clearButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection) {
					support.firePropertyChange("clear", 0, 1);
				}
			}
		});
		myText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if(!myText.getForeground().equals(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)))
					myText.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				
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
					
					// TODO: support.firePropertyChange("styleLine",0 )
					// TODO: support.firePropertyChange("color",0 )

				}
			}
		});

		combo.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				support.firePropertyChange("styleLine", -1, combo.getSelectionIndex());

			}

		});

		cs.addListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent arg0) {
				support.firePropertyChange("color", null, cs.getColorValue());
			}
		});
		
		return editMySet; 

	}

	protected void addListeners() {
		canvas.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				trafo = new Trafo(canvas);
				int u = e.x;
				int v = e.y;
				double x = trafo.convertUV(u, v)[0];
				double y = trafo.convertUV(u, v)[1];
				getStatusField().setText(String.format("(u=%04d,v=%03d);(x=%.2f,y=%.2f)", u, v, x, y));
			}
		});

	}

	void setFunctions(HashMap<String, PlotterFunction> plotterFunctions) {
		this.plotterFunctions = plotterFunctions;
	}

	HashMap<String, PlotterFunction> getFunctions() {
		return this.plotterFunctions;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName() == "AddFunction") {

			plotterFunctions = modell.getFunctions();
			this.functionList.removeAll();
			String myLabel = "";
			for (String a : plotterFunctions.keySet()) {
				this.functionList.add(plotterFunctions.get(a).getName());
				if (!myLabel.isEmpty())
					myLabel = myLabel + ", " + a;
				else
					myLabel = myLabel + a;
			}
			canvas.setFunctionLabel(myLabel);
			updateCanvas(plotterFunctions);
			if(myViewer!=null) {
				myViewer.setInput(modell);
				myViewer.refresh();
			}
		} else if (evt.getPropertyName() == "clearFunctions") {
			plotterFunctions = modell.getFunctions();
			this.functionList.removeAll();
			canvas.setFunctionLabel("");
			updateCanvas(plotterFunctions);
			if(myViewer!=null) {
				myViewer.setInput(modell);
				myViewer.refresh();
			}
		} else if (evt.getPropertyName() == "removeFunction") {
			plotterFunctions = modell.getFunctions();
			this.functionList.removeAll();
			String myLabel = "";
			for (String a : plotterFunctions.keySet()) {
				this.functionList.add(plotterFunctions.get(a).getName());
				if (!myLabel.isEmpty())
					myLabel = myLabel + ", " + a;
				else
					myLabel = myLabel + a;
			}
			canvas.setFunctionLabel(myLabel);
			updateCanvas(plotterFunctions);
			if(myViewer!=null) {
				myViewer.setInput(modell);
				myViewer.refresh();
			}
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

	public void updateCanvas(HashMap<String, PlotterFunction> fctSet) {
		canvas.setFcts(fctSet);
		canvas.redraw();
	}
}