package de.lab4inf.swt;

import static java.lang.String.format;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Basic SWT application using Composite-, Observer-, Strategy-, Template- and FactorMethod-Patterns
 * for the construction of a rudimentary UI.
 *
 * @author nwulff
 * @since  24.03.2020
 * @version $Id: SWTApplication.java,v 1.3 2020/03/27 09:02:32 nwulff Exp $
 */
public class SWTApplication {
    protected final Logger logger;
    private final Display display;
    private final Shell mainWindow;

    /**
     * POJO constructor.
     * 
     *
     */
    public SWTApplication() {
        logger = Logger.getLogger(getClass().getPackage().getName());
        display = Display.getDefault();
        mainWindow = new Shell(display, SWT.SHELL_TRIM );
        mainWindow.addDisposeListener((evt) -> setVisible(false));
        createUI();
    }

    /**
     * Get the running display of this application.
     * @return Display
     */
    public final Display getDisplay() {
        return display;
    }

    /**
     * Get the running shell of this application.
     * @return Shell
     */
    protected final Shell getShell() {
        return mainWindow;
    }

    /**
     * Template Method pattern. Creates content area, status- and toolbar, via three FactoryMethods and
     * provides a simple layout using the Composite and Stratetgy Pattern. 
     */
    private final void createUI() {
        int dd = 10;
        GridData gd;
        Rectangle bounds = display.getBounds();
        mainWindow.setBounds(bounds.x + dd, bounds.y + dd, bounds.width - 2*dd, bounds.height - 2*dd);
        mainWindow.setText(getClass().getSimpleName());
        mainWindow.setLayout(new GridLayout(1, false));
        mainWindow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        //mainWindow.disa
    	
        //Toolbar 
        ToolBar tb = createToolBar(mainWindow);
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gd.grabExcessHorizontalSpace = false;
        tb.setLayoutData(gd);
        
        
        //App Area
        Composite appArea = new Composite(mainWindow, SWT.BORDER);
        appArea.setLayout(new GridLayout(1, true));
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        appArea.setLayoutData(gd);
        
        //Status Bar
        Composite sb = createStatusBar(appArea);
        gd = new GridData(SWT.FILL, SWT.NONE, true, false);
        sb.setLayoutData(gd);

    	//Content Area
        Composite ca = createContentArea(appArea);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        ca.setLayoutData(gd);
        

        init(appArea);
    }

    /**
     * Dummy initialisation method for derived applications.
     * @param parent of the application content area.
     */
    protected void init(Composite parent) {
    }

    /**
     * Dummy method to be overloaded with your clean-up code 
     * before the application finishes. 
     */
    protected void shutDown() {
    }

    /**
     * Dummy factory method for the toolbar to be overwritten.
     * @param parent of the toolbar
     * @return ToolBar instance
     */
    protected ToolBar createToolBar(Composite parent) {
        ToolBar toolbar = new ToolBar(parent, SWT.BORDER | SWT.HORIZONTAL);
        ToolItem item = new ToolItem(toolbar, SWT.NONE);
        item.setText("Exit");
        item.addListener(SWT.Selection, (evt) -> setVisible(false));
        return toolbar;
    }

    /**
     * Dummy factory method for the statusbar to be overwritten.
     * @param parent of the statusbar
     * @return statusbar instance
     */
    protected Composite createStatusBar(Composite parent) {
        Group sb = new Group(parent, SWT.NONE);
        sb.setText("status bar");
        return sb;
    }

    /**
     * Dummy factory method for the content area to be overwritten.
     * @param parent of the content area
     * @return content area instance
     */
    protected Composite createContentArea(Composite parent) {
    	    	
        Group area = new Group(parent, SWT.FILL);
        area.setText("content area");
        area.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
        area.setLayout(new GridLayout(1, true));
        
        Label label = new Label(area, SWT.FILL);
        label.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        label.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
        label.setText("Hello World from SWT App!");

        // TODO: toogle the next line on and off to see what happens
        // label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        return area;
    }

    /**
     * Utility method to execute a task after some time asynchron to the caller.
     * @param when time to wait before execution in milliseconds
     * @param task to execute
     */
    protected final void runAsync(int when, Runnable task) {
        display.timerExec(when, task);
    }

    /**
     * Set this application visible and start a SWT event loop or shut it down.
     * @param show if true start if false shutdown the application and exit.
     */
    public final void setVisible(boolean show) {
        String appName = getClass().getSimpleName();
        if (null != mainWindow.getText())
            appName = mainWindow.getText();
        if (!mainWindow.isDisposed()) {
            if (show) {
                logger.info(format("%s event loop starts \n", appName));
                mainWindow.open();
                while (!mainWindow.isDisposed()) {
                    if (!display.readAndDispatch()) {
                        display.sleep();
                    }
                }
            } else {
                shutDown();
                mainWindow.dispose();
                logger.info(format("%s exit... \n", appName));
                System.exit(0);
            }
        }
    }

    /**
     * Bootstrap main method to start this toy application.
     * @param arguments from the command line if any
     */
    public static void main(String[] arguments) {
        SWTApplication app = new SWTApplication();
        app.setVisible(true);
    }
}
