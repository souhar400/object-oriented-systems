package de.lab4inf.swt.plotter;

import org.eclipse.swt.widgets.Composite;

import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Canvas;


public class SWTCanvasPlotter extends org.eclipse.swt.widgets.Canvas implements AffineTrafo, ResizeCanvas{

	private Color myColor;
	private int xOrigin, yOrigin; 
	private int breite, hoehe; 

	protected double uScal, vScal; 	
	protected double initXMax, initXMin; 
	
	protected double xMin, xMax,yMin, yMax; 
	private static int zoomFaktor = 1;
	

	/**
	 * Creates a new instance.
	 * 
	 * @param parent the parent.
	 * @param style  the style.
	 */
	public SWTCanvasPlotter(Composite parent, int style) {
		super(parent, style);
		//Set the draw Intervall
		double myxMin = -10; 
		double myxMax = 10; 
		double myyMin = -3; 
		double myyMax = 3; 
		
		setInitIntervall(myxMin, myxMax); 
		setDrawIntervall(myxMin, myxMax);
		setyIntervall(myyMin,myyMax); 
		addListeners();
	}
	
	
	void addListeners() {
		//MouseWheel Listener 
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {
				double xZoomSchritt = 0.5; 
				double yZoomSchritt= 0.2; 
				double[] aktualXIntervall = getIntervall(); 
				double aktualXSize= aktualXIntervall[1]-aktualXIntervall[0]; 
				
				double[] initIntervall = getInitIntervall(); 
				double initSize = initIntervall[1]-initIntervall[0]; 
				
				double[] aktualYIntervall = getyIntervall(); 
				double aktualYSize = aktualYIntervall[1]-aktualYIntervall[0]; 
				
				if (e.count > 0) {
					if(aktualXSize> 2*xZoomSchritt && aktualYSize > 2*yZoomSchritt ) { // Zoom in = Verkleinerung des Intervalls
						setDrawIntervall(aktualXIntervall[0]+xZoomSchritt, aktualXIntervall[1]-xZoomSchritt);
						setyIntervall(aktualYIntervall[0]+yZoomSchritt, aktualYIntervall[1]-yZoomSchritt);
						
					}
				} else {// Zoom out = Vergrößerung des Intervalls 
					if (aktualXSize < initSize) { //Out zoom begrenzen mit dem gewünschten Initialen Intervall
						setDrawIntervall(aktualXIntervall[0]-xZoomSchritt, aktualXIntervall[1] + xZoomSchritt);
						setyIntervall(aktualYIntervall[0]-yZoomSchritt, aktualYIntervall[1] + yZoomSchritt); 
					}

				}
				redraw();
			}
		});
		
		// Paint Listener
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {

				Canvas canvas = (Canvas) e.widget;
				int breite = canvas.getSize().x;
				int hoehe = canvas.getSize().y;
				
				// Set the drawable area
				setCanvasSize(breite, hoehe);
				
				// Scaling Factors
				double[] xIntervall = getIntervall();
				double[] yIntervall = getyIntervall(); 
				double scalU  = breite/((xIntervall[1]-xIntervall[0])); 
				double scalV = hoehe/(yIntervall[1]-yIntervall[0]); 
				setScaling(scalU, scalV); 
				
				// set the Origin
				setOrigin(breite/4, (3*hoehe)/4);

				// draw the Axis
				drawAxis(e);
				drawUnits(e);

				int[] sinPloygon = new int[2 * (getMaxU()+1)];
				Function<Double,Double> myFunc = (x) -> Math.sin(x); 
				
				// draw the functions
				int j = 0;
				for (int i=-getXOrigin(); i<=getMaxU()-getXOrigin(); i++) {		
					double zwischenI = i*((xIntervall[1]-xIntervall[0])/getMaxU()) ; 
					sinPloygon[2*j] = translateU(scalU*zwischenI);
					sinPloygon[2*j+1] = translateV(scalV*myFunc.apply(zwischenI));		
					j=j+1;
				}

				e.gc.setLineWidth(1);
				e.gc.drawPolyline(sinPloygon);
				
			}
		});
		
		
		
	}
	
	// von screen to World Coordinates 
	double[] convertUV(int u, int v) { 
		double[] xy = trafo(u,v); 
		return xy; 
	}
	
	//von world to screen Coordinates 
	int[] convertXY(double x, double y) {
		int[] uv= trafo(x,y); 
		return uv;  
	}
	
	//Set the X-Draw-Intervall 
	@Override
	public void setDrawIntervall(double xMin, double xMax){
		this.xMin = xMin; 
		this.xMax = xMax; 
	}
	
	double[] getIntervall() {
		double[] result = new double[2]; 
		result[0] = this.xMin; 
		result[1] = this.xMax; 
		return result; 
	}
	
	// Getter/Setter for X-init-Intervall 
	void setInitIntervall(double initXMin, double initXMax) {
		this.initXMin= initXMin; 
		this.initXMax=  initXMax; 
	}
	
	double[] getInitIntervall() {
		double[] initIntervall = new double[2]; 
		initIntervall[0]=this.initXMin; 
		initIntervall[1]=this.initXMax; 
		return initIntervall;
	}
	
	
	//Getter/Setter for y-draw-Intervall
	@Override
	public void setyIntervall(double yMin, double yMax) {
		this.yMin= yMin; 
		this.yMax=  yMax; 
	}
	
	double[] getyIntervall() {
		double[] yIntervall = new double[2]; 
		yIntervall[0]=this.yMin; 
		yIntervall[1]=this.yMax; 
		return yIntervall;
	}
	
	// (u,v) <-> (x,y) Operationen 	
	int translateU(double u) {
		return (int) (getXOrigin() + u); 
	}
	
	int translateU(int u) {
		return getXOrigin() + u; 
	}

	
	// Translate functions 
	int translateV(double v) {
		return (int) (getYOrigin() - v);
	}
	
	int translateV(int  v) {
		return getYOrigin() - v;
	}
	
	// Getter/Setter for origin
	@Override
	public void setOrigin(int xOrigin, int yOrigin) {
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
	}
	protected int getXOrigin() {
		return xOrigin;
	}

	protected int getYOrigin() {
		return yOrigin;
	}

	// getter/setter for scaling factors
	protected void setScaling(double uScalFactor, double vScalFactor) {
		this.uScal = uScalFactor;
		this.vScal= vScalFactor;
	}
	
	protected double[] getScaling() {
		double[] ret = new double[2]; 
		ret[0]= this.uScal ;
		ret[1] = this.vScal;
		return ret; 
	}
	
	// Getter/Setter for Canvas size
	@Override
	public void setCanvasSize(int breite, int hoehe) {
		this.breite = breite;
		this.hoehe = hoehe;
	}

	protected int getMaxU() {
		return this.breite;
	}

	protected int getMaxV() {
		return this.hoehe;
	}

	void drawUnits(PaintEvent e) {
		for (int i = 0; i <= getMaxU() - getXOrigin(); i = i + (int) uScal) {
			e.gc.drawLine(translateU(i), translateV(-3), translateU(i), translateV(3));
		}

		for (int i = 0; i >= -getXOrigin(); i = i - (int) uScal) {
			e.gc.drawLine(translateU(i), translateV(-3), translateU(i), translateV(3));
		}

		for (int i = 0; i <= getYOrigin(); i = i + (int) vScal) {
			e.gc.drawLine(translateU(-3), translateV(i), translateU(3), translateV(i));
		}

		for (int i = 0; i >= -(getMaxV() - getYOrigin()); i = i - (int) vScal) {
			e.gc.drawLine(translateU(-3), translateV(i), translateU(3), translateV(i));
		}
	}

	void drawAxis(PaintEvent e) {
		// draw the Axis
		e.gc.setLineWidth(1);
		e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		drawArrow(e.gc, 0, getYOrigin(), breite, getYOrigin(), 8, Math.toRadians(40));
		drawArrow(e.gc, getXOrigin(), hoehe, getXOrigin(), 0, 8, Math.toRadians(40));
	}

	//https://stackoverflow.com/questions/34159006/how-to-draw-a-line-with-arrow-in-swt-on-canvas
	public static void drawArrow(GC gc, int x1, int y1, int x2, int y2, double arrowLength, double arrowAngle) {
		double theta = Math.atan2(y2 - y1, x2 - x1);
		double offset = (arrowLength - 2) * Math.cos(arrowAngle);
		gc.drawLine(x1, y1, (int) (x2 - offset * Math.cos(theta)), (int) (y2 - offset * Math.sin(theta)));
		Path path = new Path(gc.getDevice());
		path.moveTo((float) (x2 - arrowLength * Math.cos(theta - arrowAngle)),
				(float) (y2 - arrowLength * Math.sin(theta - arrowAngle)));
		path.lineTo((float) x2, (float) y2);
		path.lineTo((float) (x2 - arrowLength * Math.cos(theta + arrowAngle)),
				(float) (y2 - arrowLength * Math.sin(theta + arrowAngle)));
		path.close();
		gc.fillPath(path);
		path.dispose();
	}

	/**
	 * Sets the color.
	 * 
	 * @param color the color.
	 */
	public void setColor(Color color) {
		if (this.myColor != null) {
			this.myColor.dispose();
		}
		// this.myColor = new Color(getDisplay(), color.getRGB());
		this.myColor = color;
	}

	/**
	 * Returns the color.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return this.myColor;
	}

	/**
	 * Overridden to do nothing.
	 * 
	 * @param c the color.
	 */
	public void setBackground(Color c) {
		return;
	}

	/**
	 * Overridden to do nothing.
	 * 
	 * @param c the color.
	 */
	public void setForeground(Color c) {
		return;
	}

	/**
	 * Frees resources.
	 */
	public void dispose() {
		this.myColor.dispose();
	}
	
	
	@Override
	public double[] trafo(int u, int v) {
		double[] xy = new double[2];
		xy[0] = (u - xOrigin) / uScal;
		xy[1] = (yOrigin - v) / vScal;
		return xy;
	}

	@Override
	public int[] trafo(double x, double y) {
		int[] uv = new int[2];
		uv[0] = (int) ((uScal * x) + xOrigin);
		uv[1] = (int) (yOrigin - vScal * y);
		return uv;
	}

}
