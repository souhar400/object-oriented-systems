package de.lab4inf.swt.plotter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.lab4inf.swt.WidthStrategy.ConstantStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.CurvatureStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.PrunningStepSizeStrategy;
import de.lab4inf.swt.WidthStrategy.StepSizeStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Canvas;

public class SWTCanvasPlotter extends org.eclipse.swt.widgets.Canvas implements ResizeCanvas {

	private Color myColor;
	private StepSizeStrategy strategy = new PrunningStepSizeStrategy();
	
	private boolean drawPoints = true; 

	private int xOrigin, yOrigin;
	private int breite, hoehe;
	double yZoomSchritt, xZoomSchritt;

	private boolean zoomOn = false;
	public double uScal, vScal;
	public double initXMax, initXMin;
	public boolean gestarted =false;

	public int schrittweite=1; 
	
	public double xMin, xMax, yMin, yMax;
	public HashMap<String, PlotterFunction> plotterFunctions = null;
	
	public String functionLabel=""; 
	public String getFunctionLabel() {
		return functionLabel;
	}

	public void setFunctionLabel(String functionLabel) {
		this.functionLabel = functionLabel;
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param parent the parent.
	 * @param style  the style.
	 */
	public SWTCanvasPlotter(Composite parent, int style) {
		super(parent, style);
		this.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		// Set the draw Intervall
		double myxMin = -5;
		double myxMax = 5;
		double myyMin = -3;
		double myyMax = 3;

		setInitIntervall(myxMin, myxMax);
		setDrawIntervall(myxMin, myxMax);
		setyIntervall(myyMin, myyMax);
		addListeners();
	}

	void addListeners() {
		// MouseWheel Listener
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {

				double xZoomSchritt = 0.5;
				double yZoomSchritt = 0.2;
				setZoom(xZoomSchritt, yZoomSchritt);
				double[] aktualXIntervall = getIntervall();
				double aktualXSize = aktualXIntervall[1] - aktualXIntervall[0];

				double[] initIntervall = getInitIntervall();
				double initSize = initIntervall[1] - initIntervall[0];

				double[] aktualYIntervall = getyIntervall();
				double aktualYSize = aktualYIntervall[1] - aktualYIntervall[0];

				if (e.count > 0) {
					zoomOn = true;
					if (aktualXSize > 2 * xZoomSchritt && aktualYSize > 2 * yZoomSchritt) { // rein Zoom = Verkleinerung
																							// des Intervalls
						setDrawIntervall(aktualXIntervall[0] + xZoomSchritt, aktualXIntervall[1] - xZoomSchritt);
						setyIntervall(aktualYIntervall[0] + yZoomSchritt, aktualYIntervall[1] - yZoomSchritt);

					}
				} else {// Zoom out = Vergrößerung des Intervalls
					zoomOn = false;
					setDrawIntervall(aktualXIntervall[0] - xZoomSchritt, aktualXIntervall[1] + xZoomSchritt);
					setyIntervall(aktualYIntervall[0] - yZoomSchritt, aktualYIntervall[1] + yZoomSchritt);
					if (aktualXSize < initSize) { // Out zoom begrenzen mit dem gewünschten Initialen Intervall
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
				double scalU = breite / ((xIntervall[1] - xIntervall[0]));
				double scalV = hoehe / (yIntervall[1] - yIntervall[0]);
				setScaling(scalU, scalV);

				// set the Origin
				if(!gestarted)
				{
					setOrigin(breite / 2, hoehe / 2);
					gestarted = true; 
				}

				// draw the Axis
				drawAxis(e);
				drawUnits(e);
				drawBeschrift(e);
				drawLabels(e); 
				if (plotterFunctions != null)
					drawFunction(e);
			}
		});

	}

	void setZoom(double xZoom, double yZoom) {
		this.xZoomSchritt = xZoom;
		this.yZoomSchritt = yZoom;
	}
	void drawFunction(PaintEvent e) {
		for(PlotterFunction fct : plotterFunctions.values()) {
			int[] color = fct.getColor();
			
			int[] polygon = strategy.calculatePoints(this, fct);
			
			e.gc.setLineWidth(1);
			e.gc.setForeground(new Color(null, color[0], color[1], color[2]));  
			e.gc.setLineStyle(fct.getLineStyle()); 
			if( drawPoints)
				for(int i =0; i<polygon.length; i=i+2 )
					e.gc.drawRectangle(polygon[i]-1, polygon[i+1]-1, 2, 2);
			e.gc.drawPolyline(polygon);
		}
	}

//	void drawFunction(PaintEvent e) {
//
//		double[] xIntervall = getIntervall();
//		double[] yIntervall = getyIntervall();
//
//		double scalU = breite / ((xIntervall[1] - xIntervall[0]));
//		double scalV = hoehe / (yIntervall[1] - yIntervall[0]);
//
//		for (PlotterFunction fct : plotterFunctions.values()) {
//			Function<Double, Double> myFct = fct.getFunction(); 
//
//			List<Integer> list = new ArrayList<Integer>();
//			for (int k = -getXOrigin(); k <= getMaxU() - getXOrigin(); k = k + 1) {
//				double zwischenK = k * ((xIntervall[1] - xIntervall[0]) / getMaxU());
//				int yPixel = (int) (scalV * myFct.apply(zwischenK));
//				
//				if (!(yPixel < -getMaxV() || yPixel > getMaxV() || Double.isNaN(myFct.apply(zwischenK)))) {
//					list.add(translateU(zwischenK * scalU));
//					list.add(translateV(scalV * myFct.apply(zwischenK)));
//				}
//			}
//
//			int[] polygon = new int[list.size()];
//			for (int i = 0; i < list.size(); i++)
//				polygon[i] = list.get(i);
//
//			e.gc.setLineWidth(2);
//			int[] color = fct.getColor();
//			e.gc.setForeground(new Color(null, color[0], color[1], color[2]));  
//			e.gc.setLineStyle(fct.getLineStyle()); 
//			e.gc.drawPolyline(polygon);
//		}
//	}

	// Set the X-Draw-Intervall
	@Override
	public void setDrawIntervall(double xMin, double xMax) {
		this.xMin = xMin;
		this.xMax = xMax;
	}

	public double[] getIntervall() {
		double[] result = new double[2];
		result[0] = this.xMin;
		result[1] = this.xMax;
		return result;
	}

	// Getter/Setter for X-init-Intervall
	void setInitIntervall(double initXMin, double initXMax) {
		this.initXMin = initXMin;
		this.initXMax = initXMax;
	}

	double[] getInitIntervall() {
		double[] initIntervall = new double[2];
		initIntervall[0] = this.initXMin;
		initIntervall[1] = this.initXMax;
		return initIntervall;
	}

	// Getter/Setter for y-draw-Intervall
	@Override
	public void setyIntervall(double yMin, double yMax) {
		this.yMin = yMin;
		this.yMax = yMax;
	}

	public double[] getyIntervall() {
		double[] yIntervall = new double[2];
		yIntervall[0] = this.yMin;
		yIntervall[1] = this.yMax;
		return yIntervall;
	}

	// (u,v) <-> (x,y) Operationen
	public int translateU(double u) {
		return (int) (getXOrigin() + u);
	}

	int translateU(int u) {
		return getXOrigin() + u;
	}

	// Translate functions
	public int translateV(double v) {
		return (int) (getYOrigin() - v);
	}

	int translateV(int v) {
		return getYOrigin() - v;
	}

	// Getter/Setter for origin
	@Override
	public void setOrigin(int xOrigin, int yOrigin) {
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
	}

	public int getXOrigin() {
		return xOrigin;
	}

	public int getYOrigin() {
		return yOrigin;
	}

	// getter/setter for scaling factors
	public void setScaling(double uScalFactor, double vScalFactor) {
		this.uScal = uScalFactor;
		this.vScal = vScalFactor;
	}

	public double[] getScaling() {
		double[] ret = new double[2];
		ret[0] = this.uScal;
		ret[1] = this.vScal;
		return ret;
	}

	// Getter/Setter for Canvas size
	@Override
	public void setCanvasSize(int breite, int hoehe) {
		this.breite = breite;
		this.hoehe = hoehe;
	}

	public int getMaxU() {
		return this.breite;
	}

	public int getMaxV() {
		return this.hoehe;
	}

	void drawBeschrift(PaintEvent e) {

		Font font = new Font(e.gc.getDevice(), "Arial", 8, SWT.NONE);
		e.gc.setFont(font);
		e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
		font.dispose();
		double j = 1.0*schrittweite;
		for (int i = ((int) uScal)*schrittweite; i <= getMaxU() - getXOrigin(); i = i + ((int) uScal)*schrittweite) {
			String label = String.valueOf(j);
			e.gc.drawString(label, translateU(i - 6), translateV(-7), true);
			j = j + schrittweite;
		}
		
		j = -1.0*schrittweite;		
		for (int i = -((int)uScal)*schrittweite; i >= -getXOrigin(); i = i - ((int) uScal)*schrittweite) {
			String label = String.valueOf(j);
			e.gc.drawString(label, translateU(i - 10), translateV(-7), true);
			j = j - schrittweite;
		}
		
		j=1.0*schrittweite; 
		for (int i = ((int)vScal)*schrittweite; i <= getYOrigin(); i = i + ((int) vScal)*schrittweite) {
			String label = String.valueOf(j);
			e.gc.drawString(label, translateU(- 20), translateV(i+5), true);
			j = j + schrittweite;
		}
		
		j=-1.0*schrittweite;
		for (int i = ((int)-vScal)*schrittweite; i >= -(getMaxV() - getYOrigin()); i = i - ((int) vScal)*schrittweite){
			String label = String.valueOf(j);
			e.gc.drawString(label, translateU(- 25), translateV(i+5), true);
			j = j - schrittweite;
		}
		
		font = new Font(e.gc.getDevice(), "Arial", 15, SWT.NONE);
		e.gc.setFont(font);
		font.dispose();
		e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
	}
	
	void drawLabels(PaintEvent e) {
		e.gc.drawString("x", translateU(getMaxU() - getXOrigin() - 30),  translateV(25), true); 
		e.gc.drawString(functionLabel, translateU(5),  translateV(getMaxV() - getYOrigin() - 10), true);
	}

	void drawUnits(PaintEvent e) {

		for (int i = 0; i <= getMaxU() - getXOrigin(); i = i + ((int) uScal)*schrittweite) {
			e.gc.drawLine(translateU(i), translateV(-3), translateU(i), translateV(3));
		}

		for (int i = 0; i >= -getXOrigin(); i = i - ((int) uScal)*schrittweite) {
			e.gc.drawLine(translateU(i), translateV(-3), translateU(i), translateV(3));

		}

		for (int i = 0; i <= getYOrigin(); i = i + ((int) vScal)*schrittweite) {
			e.gc.drawLine(translateU(-3), translateV(i), translateU(3), translateV(i));
		}

		for (int i = 0; i >= -(getMaxV() - getYOrigin()); i = i - ((int) vScal)*schrittweite) {
			e.gc.drawLine(translateU(-3), translateV(i), translateU(3), translateV(i));
		}
	}

	void drawAxis(PaintEvent e) {

		// draw the Axis
		e.gc.setLineWidth(3);
		e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		drawArrow(e.gc, 0, getYOrigin(), breite, getYOrigin(), 8, Math.toRadians(40));
		drawArrow(e.gc, getXOrigin(), hoehe, getXOrigin(), 0, 8, Math.toRadians(40));

	}

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

	public void setFcts(HashMap<String, PlotterFunction> fctSet) {
		this.plotterFunctions= fctSet;
	}

	public HashMap<String, PlotterFunction> getFcts() {
		return this.plotterFunctions;
	}

	/**
	 * Frees resources.
	 */
	public void dispose() {
		this.myColor.dispose();
	}

}
