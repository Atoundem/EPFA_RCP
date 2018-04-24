package org.icipe.epfa.connectiontor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;


public class InstallRequirementsUI {
	
	Display display = Display.getDefault();
	protected Shell shlSystemRequirementsInstallation;
	public Table table;
	private Text txtCdRequirements;
	public static ProgressBar progressBar;
	
	
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
		shlSystemRequirementsInstallation.setSize(450, 294);
		shlSystemRequirementsInstallation.setText("System Requirements Installation");
		
		table = new Table(shlSystemRequirementsInstallation, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		table.setBounds(10, 66, 424, 100);
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
		
		Button btnBrowseInstallers = new Button(shlSystemRequirementsInstallation, SWT.NONE);
		btnBrowseInstallers.setFont(SWTResourceManager.getFont("Comic Sans MS", 8, SWT.NORMAL));
		btnBrowseInstallers.setToolTipText("Look for \"EPFA/Requirements\"");
		btnBrowseInstallers.setBounds(10, 35, 110, 25);
		btnBrowseInstallers.setText("Browse installers :");
		
		txtCdRequirements = new Text(shlSystemRequirementsInstallation, SWT.BORDER);
		txtCdRequirements.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		txtCdRequirements.setText("CD:\\\\ Requirements\\\\");
		txtCdRequirements.setBounds(126, 36, 308, 23);
		
		Button btnInstall = new Button(shlSystemRequirementsInstallation, SWT.NONE);
		btnInstall.setBounds(366, 190, 68, 23);
		btnInstall.setText("Install");
		
		progressBar = new ProgressBar(shlSystemRequirementsInstallation, SWT.NONE);
		progressBar.setBounds(10, 175, 424, 12);
		
		Label lblCautionYouMust = new Label(shlSystemRequirementsInstallation, SWT.NONE);
		lblCautionYouMust.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblCautionYouMust.setFont(SWTResourceManager.getFont("Comic Sans MS", 12, SWT.NORMAL));
		lblCautionYouMust.setBounds(10, 192, 315, 21);
		lblCautionYouMust.setText("Caution! Install R in C:\\ as shown below");
		
		Label label = new Label(shlSystemRequirementsInstallation, SWT.BORDER);
		label.setAlignment(SWT.CENTER);
		label.setImage(ResourceManager.getPluginImage("org.icipe.epfa.connectiontor", "icons/R path4.png"));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setBounds(10, 217, 424, 44);
		
		btnInstall.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strPathInstallers = txtCdRequirements.getText();
				int itemToInstall = table.getSelectionIndex();
				
				if(strPathInstallers.equalsIgnoreCase("")){
					MessageDialog.openError(shlSystemRequirementsInstallation, "System Requirements", "Select the installers path");
					txtCdRequirements.setFocus();
					return;
				}
				if(itemToInstall < 0){
					MessageDialog.openError(shlSystemRequirementsInstallation, "System Requirements", "Select one item in the Requirements table");
					table.setFocus();
					return;
				}
				
				
				Rserve.installSoftwares(strPathInstallers, itemToInstall, table);
			}
		});
		
		btnBrowseInstallers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shlSystemRequirementsInstallation, SWT.OPEN);
				dialog.setFilterPath("\\EPFA\\Requirements");
				dialog.open();
				
				txtCdRequirements.setText(dialog.getFilterPath());
			}
		});
		
		
	}
}
