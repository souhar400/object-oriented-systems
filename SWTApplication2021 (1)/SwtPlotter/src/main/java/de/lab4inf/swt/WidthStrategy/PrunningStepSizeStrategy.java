package de.lab4inf.swt.WidthStrategy;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import de.lab4inf.swt.plotter.PlotterFunction;
import de.lab4inf.swt.plotter.SWTCanvasPlotter;
import de.lab4inf.swt.plotter.Trafo;

public class PrunningStepSizeStrategy implements StepSizeStrategy {

	@Override
	public int[] calculatePoints(SWTCanvasPlotter canvas, PlotterFunction fct) {

		double maxX = canvas.getIntervall()[1];
		double minX = canvas.getIntervall()[0];
		double width = canvas.getMaxU();
		double size = maxX - minX;
		double step = (size / width);

		double hoehe = canvas.getMaxV();

		double error = 1 / width;
		TreeMap<Double, Double> xy = new TreeMap<Double, Double>();
		Function<Double, Double> func = fct.getFunction();
		Trafo transformer = new Trafo(canvas);

		double prevY = func.apply(minX);
		double prevX = minX;
		if (!(Double.isNaN(prevY) || (int) (canvas.getYOrigin() - prevY) > hoehe || (int)(canvas.getYOrigin() - prevY) < -hoehe))
			xy.put(minX, prevY);

		for (double x = minX + step; x < maxX; x += step) {
			double y = func.apply(x);
			int yPixel = (int) (canvas.getYOrigin() - y);

			if (Double.isNaN(y) || yPixel < -hoehe || yPixel > hoehe)
				continue;
			else if (Double.isNaN(prevY) || (int)(canvas.getYOrigin() - prevY) < -hoehe || (int)(canvas.getYOrigin() - prevY) > hoehe) {
				prevY = canvas.getyIntervall()[1]*30;
				prevX = x - 2 * step;
			}
			double yMid = func.apply((x + prevX) / 2);
			if (Math.abs(((y + prevY) / 2) - yMid) > error) {
				if (x - prevX <= step) {
					double xMax = x, xMin = x, yMax = y, yMin = y;
					for (double ix = x - (step / 2.); ix < x + (step / 2.); ix += step / 10.) {
						double iy = func.apply(ix);
						if (iy > yMax) {
							yMax = iy;
							xMax = ix;
						} else if (iy < yMin) {
							yMin = iy;
							xMin = ix;
						}
					}

					if (!(Double.isNaN(yMin) || (int)(canvas.getYOrigin() - yMin) > hoehe
							|| (int)(canvas.getYOrigin() - yMin) < -hoehe || Double.isNaN(yMax)
							|| (int)(canvas.getYOrigin() - yMax) > hoehe ||(int) (canvas.getYOrigin() - yMax) < -hoehe)) {

						if (xMax != xMin) {
							xy.put(xMax, yMax);
							xy.put(xMin, yMin);
						} else
							xy.put(xMax, yMax);
					}
				} else
					xy.put(x, y);
				prevY = y;
				prevX = x;
			}
		}
		prevY = func.apply(maxX);
		if (!(Double.isNaN(prevY) || (int)(canvas.getYOrigin() - prevY) > hoehe
				|| (int)(canvas.getYOrigin() - prevY) < -hoehe))
			xy.put(maxX, prevY);
		int[] xyArray = new int[xy.size() * 2];
		int i = 0;
		int[] point;
		for (Map.Entry<Double, Double> entry : xy.entrySet()) {
			point = transformer.convertXY(entry.getKey(), entry.getValue());
			xyArray[i] = point[0];
			xyArray[i + 1] = point[1];
			i += 2;
		}

		return xyArray;
	}
}
