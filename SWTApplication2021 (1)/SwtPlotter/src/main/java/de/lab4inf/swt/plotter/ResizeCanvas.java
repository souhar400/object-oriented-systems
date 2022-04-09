package de.lab4inf.swt.plotter;

interface ResizeCanvas {

	void setCanvasSize(int breite, int hoehe);
	void setOrigin(int xOrigin, int yOrigin);
	void setDrawIntervall(double xMin,double xMax);
	void setyIntervall(double yMin, double yMax);
}
