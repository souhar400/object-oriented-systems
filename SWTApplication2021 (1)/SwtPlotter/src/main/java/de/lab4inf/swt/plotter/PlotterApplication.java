package de.lab4inf.swt.plotter;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.lab4inf.swt.SWTApplication;

public class PlotterApplication extends SWTApplication{
	
	Label statusField;
	SWTCanvasPlotter canvas; 
	
	public PlotterApplication() {
		super(); 
	}
	
	
	public void setStatusField(Label statusField) {
		this.statusField=statusField; 
	}
	
	private Label getStatusField() {
		return this.statusField;
	}
	
	public void setCanvas( SWTCanvasPlotter myCanvas) {
		this.canvas= myCanvas;
	}
	
	public SWTCanvasPlotter getCanvas( ) {
		return this.canvas; 
	}
	
	// ToolBar 
    protected ToolBar createToolBar(Composite parent) {
        ToolBar toolbar = new ToolBar(parent, SWT.BORDER | SWT.HORIZONTAL);
        ToolItem exitItem = new ToolItem(toolbar, SWT.NONE);
        exitItem.setText("Exit");
        
        ToolItem helpItem = new ToolItem(toolbar, SWT.NONE);
        helpItem.setText("Help");
        
        exitItem.addListener(SWT.Selection, (evt) -> setVisible(false));
        return toolbar;
    }

    
    //StatusBar
    protected Composite createStatusBar(Composite parent) {
        GridData gd ; 
    	Group sb = new Group(parent, SWT.NONE);
        sb.setText("StatusBar");
        sb.setLayout(new GridLayout(1, false ));
        
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

    //Content Area
    protected Composite createContentArea(Composite parent) {
        Group area = new Group(parent, SWT.NONE);
        area.setText("PlotCanvas");
        area.setLayout(new FillLayout());
        SWTCanvasPlotter myCanvas = new SWTCanvasPlotter(area, SWT.None);  
        setCanvas(myCanvas);
        
        myCanvas.addMouseMoveListener(new MouseMoveListener() {
    		@Override
    		public void mouseMove(MouseEvent e) {
    			int u= e.x; 
    			int v= e.y; 
    			double x = myCanvas.convertUV(u, v)[0]; 
    			double y = myCanvas.convertUV(u, v)[1]; 
    			getStatusField().setText(String.format("(u=%04d,v=%03d);(x=%.2f,y=%.2f)", u, v,x,y ));
    		}
    	});
        return area;
    }
    
    public static void main(String[] arguments) {
        SWTApplication app = new PlotterApplication();
        app.setVisible(true);
    }
}
