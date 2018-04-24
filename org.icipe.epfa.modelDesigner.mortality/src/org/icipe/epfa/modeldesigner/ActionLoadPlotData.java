package org.icipe.epfa.modeldesigner;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.icipe.epfa.modeldesigner.windows.LoadPlotData;

public class ActionLoadPlotData implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {

		LoadPlotData fenetreFille = new LoadPlotData(Display.getCurrent().getActiveShell(),SWT.SHELL_TRIM);
//		LoadPlotData fenetreFille = new LoadPlotData(Display.getCurrent().getActiveShell(),SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		fenetreFille.open();

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

}
