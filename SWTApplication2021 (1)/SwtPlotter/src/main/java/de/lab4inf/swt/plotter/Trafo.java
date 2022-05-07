package de.lab4inf.swt.plotter;

public class Trafo implements AffineTrafo {
	private double uScal, vScal;
	private int xOrigin, yOrigin;
	public Trafo(SWTCanvasPlotter canvas) {
		xOrigin = canvas.getXOrigin();
		yOrigin = canvas.getYOrigin();
		uScal = canvas.getScaling()[0];
		vScal = canvas.getScaling()[1];
	}
	
	@Override
	public int[] trafo(double x, double y) {
		int[] uv = new int[2];
		uv[0] = (int) ((uScal * x) + xOrigin);
		uv[1] = (int) (yOrigin - vScal * y);
		return uv;
	}

	@Override
	public double[] trafo(int u, int v) {
		double[] xy = new double[2];
		xy[0] = (u - xOrigin) / uScal;
		xy[1] = (yOrigin - v) / vScal;
		return xy;
	}

	// von screen to World Coordinates
	public double[] convertUV(int u, int v) {
		double[] xy = trafo(u, v);
		return xy;
	}

	// von world to screen Coordinates
	public int[] convertXY(double x, double y) {
		int[] uv = trafo(x, y);
		return uv;
	}
}
