package org.icipe.epfa.mapping;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.icipe.epfa.mapping.windows.MappingDialog;

public class ActionGenerateMap implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		MappingDialog mapWing = new MappingDialog( Display.getCurrent().getActiveShell(),SWT.SHELL_TRIM);
		//fenetreFille.setSize(200, 200);
		mapWing.open();

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
