package de.lab4inf.rcp.plotter;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PlotterPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.createFolder("left", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		layout.createFolder("top", IPageLayout.TOP, 1.0f, IPageLayout.ID_EDITOR_AREA);
		layout.createFolder("right", IPageLayout.RIGHT, 0.2f, IPageLayout.ID_EDITOR_AREA);
	}

}
