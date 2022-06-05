package de.lab4inf.rcp.plotter.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import de.lab4inf.swt.WidthStrategy.ConstantStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.CurvatureStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.DivideAndConquerStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.ErrorStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.PrunningStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.StepSizeStrategy;
import de.lab4inf.swt.plotter.PlotterFunction;

public class EditorView extends ViewPart {
	private Group parentGroup;
	private Text functionText;
	private Button addButton;
	private Button removeButton;
	private Button clearButton;
	private ModelProvider provider;
	private ISelectionService service;
	private String strategy;

	@PostConstruct
	@Override
	public void createPartControl(Composite parent) {
		service = getViewSite().getWorkbenchWindow().getSelectionService();

		provider = ModelProvider.getInstance();
		Group editMySet = new Group(parent, SWT.BORDER);
		editMySet.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true);
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
		// myXminText.setText(String.valueOf(canvas.getIntervall()[0]));
		myXminText.setLayoutData(gd);

		Label xMaxLabel = new Label(coorSystem, SWT.NONE);
		xMaxLabel.setText("Xmax ");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		xMaxLabel.setLayoutData(gd);

		Text myXmaxText = new Text(coorSystem, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		// myXmaxText.setText(String.valueOf(canvas.getIntervall()[1]));
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

		Button updateButton = new Button(coorSystem, SWT.PUSH);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 6;
		updateButton.setLayoutData(gd);
		updateButton.setText("Update");

		
		Label strategyLabel = new Label(editMySet, SWT.NONE);
		strategyLabel.setText("Strategy");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		strategyLabel.setLayoutData(gd);
		
		
		Combo combo = new Combo(editMySet, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		combo.setLayoutData(gd);
		combo.setItems(new String[] { "Curvature", "Pruning", "Error rate", "Divide and conquer", "Constant step size"});
		combo.select(0);
		
		
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(combo.getSelectionIndex()) {
				case 0:
					strategy = "Curvature";
					break; 
				case 1:
					strategy = "Pruning";
					break; 
				case 2:
					strategy = "Error";
					break; 
				case 3:
					strategy = "DivideAndConquer";
					break; 
				case 4: 
					strategy = "Constant";
					break; 
				}
				fireStrategyChanged(strategy);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Group addRemoveClean = new Group(editMySet, SWT.BEGINNING);
		addRemoveClean.setLayout(new GridLayout(3, true));
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		addRemoveClean.setLayoutData(gd);
		
		Combo combo2 = new Combo(addRemoveClean, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(SWT.RIGHT, SWT.FILL, false, false);
		combo2.setLayoutData(gd);
		combo2.setItems(new String[] { "Solid", "Dash", "Dot", "DashDotDot", "DashDot" });
		combo2.select(0);

		ColorSelector cs = new ColorSelector(addRemoveClean);
		cs.setEnabled(true);

		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;
		
		functionText = new Text(addRemoveClean, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 3;
		functionText.setLayoutData(gd);
		
		
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 3;

		addButton = new Button(addRemoveClean, SWT.PUSH);
		addButton.setLayoutData(gd);
		addButton.setText("Add");

		removeButton = new Button(addRemoveClean, SWT.PUSH);
		removeButton.setLayoutData(gd);
		removeButton.setText("Remove");

		clearButton = new Button(addRemoveClean, SWT.PUSH);
		clearButton.setLayoutData(gd);
		clearButton.setText("Clear");

		parentGroup = editMySet;
		addListeners();
	}

	public void addListeners() {
		removeButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection) {
					PlotterFunction pf;
					TreeSelection sel = (TreeSelection) service.getSelection("de.lab4inf.rcp.plotter.parts.ListView");
					Object selectedObject = sel.getFirstElement();
					if (selectedObject instanceof PlotterFunction) {
						pf = (PlotterFunction) selectedObject;
						firePartPropertyChanged("removeFunction", null, pf.getName());
					}

				}
			}
		});
		addButton.addListener(SWT.Selection, event -> firePartPropertyChanged("addFunction", null, functionText.getText()));
		clearButton.addListener(SWT.Selection, event -> firePartPropertyChanged("clear", null, null));

	}

	@Override
	public void setFocus() {
		parentGroup.setFocus();

	}
	private void fireStrategyChanged(String strategy) {
		firePartPropertyChanged("strategy", null, strategy);
	}

}
