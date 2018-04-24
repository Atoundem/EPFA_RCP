package org.icipe.epfa.modeldesigner;

import java.awt.image.BufferedImage;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.SWTResourceManager;

import org.icipe.epfa.classUtils.EPFAUtils;
import org.icipe.epfa.classUtils.ImageSelection;

public class Sharpe1UI extends Dialog {

	protected Object result;
	public Shell shell;
	protected int model;
	protected String path, stageSel;
	

	public Browser bwrResultDR;
	public Label lblFunctImageDR;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Sharpe1UI(Shell parent, int style) {
		super(parent, style);
	}
	
	public Sharpe1UI(Shell parent, int model, String path, String stageSel) {
		super(parent);
		this.model = model;
		this.path = path;
		this.stageSel = stageSel;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	public void createContents(String title) {
		shell = new Shell(getParent(), SWT.MIN);
		shell.setLayout(new FormLayout());
		shell.setSize(490, 590);
		shell.setText(title);

		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		final FormData formData = new FormData();
		formData.bottom = new FormAttachment(100, -39);
		formData.right = new FormAttachment(100, -8);
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		tabFolder.setLayoutData(formData);
		
		final TabItem graphOutputsTabItem = new TabItem(tabFolder, SWT.NONE);
		graphOutputsTabItem.setText("Graph Output");

		lblFunctImageDR = new Label(tabFolder, SWT.NONE);
		lblFunctImageDR.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		graphOutputsTabItem.setControl(lblFunctImageDR);
		
		final TabItem statisticalOutputTabItem = new TabItem(tabFolder, SWT.NONE);
		statisticalOutputTabItem.setText("Statistical Output");

		bwrResultDR = new Browser(tabFolder, SWT.NONE);
		statisticalOutputTabItem.setControl(bwrResultDR);

		Button btnAccept = new Button(shell, SWT.NONE);
		final FormData formData_1 = new FormData();
		formData_1.left = new FormAttachment(0, 295);
		formData_1.right = new FormAttachment(0, 380);
		btnAccept.setLayoutData(formData_1);
		btnAccept.setText("&Accept");

		Button btnCancel = new Button(shell, SWT.NONE);
		formData_1.bottom = new FormAttachment(btnCancel, 24, SWT.TOP);
		formData_1.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		final FormData formData_2 = new FormData();
		formData_2.left = new FormAttachment(0, 390);
		formData_2.bottom = new FormAttachment(tabFolder, 29, SWT.BOTTOM);
		formData_2.top = new FormAttachment(tabFolder, 5, SWT.BOTTOM);
		formData_2.right = new FormAttachment(0, 475);
		btnCancel.setLayoutData(formData_2);
		btnCancel.setText("&Cancel");
		
		final Menu menu = new Menu(lblFunctImageDR);
		lblFunctImageDR.setMenu(menu);
		
		final MenuItem copyMenuItem = new MenuItem(menu, SWT.NONE);
		copyMenuItem.setText("Copy");
		final MenuItem propImgMenuItem = new MenuItem(menu, SWT.NONE);
		propImgMenuItem.setText("Properties");
		final MenuItem restoreImgMenuItem = new MenuItem(menu, SWT.NONE);
		restoreImgMenuItem.setText("Restore");
		
		propImgMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ModifyImageUI ui = new ModifyImageUI(shell);
				ui.model = model;
				ImageProperties ip = ModifyImageUI.ip;
				ui.ModifyImageUIVar(path, lblFunctImageDR,
						ip.getCorrX1(),ip.getCorrX2(),ip.getCorrY1(),ip.getCorrY2(),ip.getMini(),
						ip.getMaxi(), ip.getLabX(),ip.getLabY(),ip.getTitle(),ip.getLegX(),ip.getLegY(), ip.getScaleY(), ip.getScaleX());
				ui.open();
			}
		});
		restoreImgMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DevelopmentRate.restoreImage(stageSel, path, lblFunctImageDR, model);
			}
		});
		copyMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				BufferedImage image = EPFAUtils.convertToAWT(new ImageData(path));
				ImageSelection.copyImageToClipboard(image);
			}
		});
		
		btnAccept.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				boolean finish = false;
				finish = MessageDialog.openConfirm(shell, "Finish Mortality",
						"Are you sure to choose this function: "+shell.getText());
				
				if(finish)
					DevelopmentRate.setModelSelected(model);
				
			}
		});
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

}
