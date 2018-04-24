package org.icipe.epfa.connectiontor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

public class InstallRequirementsMacUI {
	
	Display display = Display.getDefault();
	protected Shell shlSystemRequirementsInstallation;
	public Table table;
	
	public void open() {
		shlSystemRequirementsInstallation.open();
		shlSystemRequirementsInstallation.layout();
		while (!shlSystemRequirementsInstallation.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContents() {
		shlSystemRequirementsInstallation = new Shell(display.getActiveShell(), SWT.MIN | SWT.CLOSE);
		shlSystemRequirementsInstallation.setSize(450, 326);
		shlSystemRequirementsInstallation.setText("System Requirements Installation");
		
		table = new Table(shlSystemRequirementsInstallation, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		table.setBounds(10, 36, 424, 250);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnSoftware = new TableColumn(table, SWT.NONE);
		tblclmnSoftware.setWidth(314);
		tblclmnSoftware.setText("Software");
		
		TableColumn tblclmnState = new TableColumn(table, SWT.NONE);
		tblclmnState.setWidth(100);
		tblclmnState.setText("State");
		
		Label lblSoftwareToInstall = new Label(shlSystemRequirementsInstallation, SWT.NONE);
		lblSoftwareToInstall.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblSoftwareToInstall.setBounds(10, 10, 130, 18);
		lblSoftwareToInstall.setText("Software to install :");
		
		
	}

}
