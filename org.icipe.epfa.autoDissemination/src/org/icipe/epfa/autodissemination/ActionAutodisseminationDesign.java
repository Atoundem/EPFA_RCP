package org.icipe.epfa.autodissemination;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.icipe.epfa.autodissemination.MainActionAutoDiss;

public class ActionAutodisseminationDesign implements
		IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		MainActionAutoDiss.launchWizard();

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
