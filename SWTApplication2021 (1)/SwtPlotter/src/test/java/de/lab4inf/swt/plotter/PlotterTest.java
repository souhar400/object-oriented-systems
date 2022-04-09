package de.lab4inf.swt.plotter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlotterTest {
	PlotterApplication myApp;
	private final static Display display = Display.getDefault();
	static Shell mainWindow = new Shell(display, SWT.SHELL_TRIM | SWT.H_SCROLL | SWT.V_SCROLL);
	SWTCanvasPlotter canvas = new SWTCanvasPlotter(mainWindow, SWT.NONE);

	int xOrigin, yOrigin;
	double uScal, vScal;
	int uMin=0, uMax;
	int vMin=0, vMax;
	double xMin, yMin;
	double xMax, yMax;

	@BeforeEach
	public void setUp() {
		canvas.setCanvasSize(500, 500);
		canvas.setSize(500, 500);
		canvas.setOrigin(250, 250);
		canvas.setDrawIntervall(-10, 10);
		canvas.setyIntervall(-5.0, 5.0);

		uScal = canvas.getSize().x / (canvas.getIntervall()[1] - canvas.getIntervall()[0]);
		vScal = canvas.getSize().y / (canvas.getyIntervall()[1] - canvas.getyIntervall()[0]);
		canvas.setScaling(uScal, vScal);

		xOrigin = canvas.getXOrigin();
		yOrigin = canvas.getYOrigin();

		uMax = canvas.getMaxU();
		vMax = canvas.getMaxV();
		xMin = canvas.xMin;
		xMax = canvas.xMax;
		yMin = canvas.yMin; 
		yMax = canvas.yMax; 
	}

	@Test
	void canvasTest() {
		assertNotNull(canvas);
	}

	
	@Test
	void intOriginTrafo() {
		int[] uv = canvas.trafo(0.0, 0.0);
		assertEquals(xOrigin, uv[0]);
		assertEquals(yOrigin, uv[1]);
	}
	
	
	@Test
	void doubleTrafo() {
		double[] uv = canvas.trafo(uMin, vMax - yOrigin);
		assertEquals(xMin, uv[0]);
		assertEquals(0, uv[1]);
	}

	@Test
	void doubleTrafo2() {
		double[] uv = canvas.trafo(xOrigin, 0);
		assertEquals(0, uv[0]);
		assertEquals(yMax, uv[1]);
	}
	
	@Test
	void yObenTrafoTest() {
		int[] uv = canvas.trafo(0.0, yMax);
		assertEquals(xOrigin, uv[0]);
		assertEquals(0, uv[1]);
	}

	@Test
	void convertUVTest() {
		Random rdm = new Random(); 
		int myU = rdm.nextInt(500);
		int myV = rdm.nextInt(500);
		double[] myResult = canvas.trafo(myU, myV);
		int[] actualResult = canvas.trafo(myResult[0], myResult[1]);
		assertEquals(myU, actualResult[0]);
		assertEquals(myV, actualResult[1]);
	}
}
