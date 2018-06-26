package org.icipe.epfa.autodissemination.wizards;

import org.icipe.epfa.autodissemination.AutoDissemination;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;

public class OneModelWizardPage1 extends WizardPage {

	public static Text textAlgoSelected;
	public static Label lblSelectedFunctionAlone;
	public static Text txtAdjust, txtPar1, txtPar2, txtPar3, txtPar4, txtPar5, txtPar6, txtPar7;
	static Text txtTminTempOne, txtTminValueOne;
	public static Label lblImageTemp, lblPar1, lblPar2, lblPar3, lblPar4, lblPar5, lblPar6, lblPar7;
	private static Button chkLimitsOne;
	public static Label lblStageSelected;
	public static Text textStageSelected;
	
	/**
	 * Create the wizard.
	 */
	public OneModelWizardPage1() {
		super("OneModelWizardPage1");
		setTitle("One model selected");
		setDescription("Change the parameters or set this model");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FormLayout());
		
		Label lblSelectedAlgo = new Label(container, SWT.NONE);
		lblSelectedAlgo.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		FormData fd_lblSelectedAlgo = new FormData();
		fd_lblSelectedAlgo.top = new FormAttachment(0, 10);
		lblSelectedAlgo.setLayoutData(fd_lblSelectedAlgo);
		lblSelectedAlgo.setText("Algorithm selected:");
		
		textAlgoSelected = new Text(container, SWT.NONE);
		textAlgoSelected.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		textAlgoSelected.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		FormData fd_textAlgoSelected = new FormData();
		fd_textAlgoSelected.top = new FormAttachment(lblSelectedAlgo, 0, SWT.TOP);
		fd_textAlgoSelected.left = new FormAttachment(lblSelectedAlgo, 6);
		fd_textAlgoSelected.right = new FormAttachment(100, -221);
		textAlgoSelected.setLayoutData(fd_textAlgoSelected);
		
		Label lblFunctions = new Label(container, SWT.NONE);
		fd_lblSelectedAlgo.left = new FormAttachment(lblFunctions, 0, SWT.LEFT);
		lblFunctions.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		FormData fd_lblFunctions = new FormData();
		fd_lblFunctions.top = new FormAttachment(lblSelectedAlgo, 30);
		fd_lblFunctions.left = new FormAttachment(0, 10);
		lblFunctions.setLayoutData(fd_lblFunctions);
		lblFunctions.setText("Selected function : ");
		
		lblSelectedFunctionAlone = new Label(container, SWT.NONE);
		lblSelectedFunctionAlone.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		FormData fd_cboFunctions = new FormData();
		fd_cboFunctions.top = new FormAttachment(lblFunctions, 0, SWT.TOP);
		fd_cboFunctions.left = new FormAttachment(lblFunctions, 16);
		fd_cboFunctions.right = new FormAttachment(44);
		lblSelectedFunctionAlone.setLayoutData(fd_cboFunctions);
		
		lblImageTemp = new Label(container, SWT.BORDER | SWT.CENTER);
		lblImageTemp.setAlignment(SWT.CENTER);
		lblImageTemp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_lblImageTemp = new FormData();
		fd_lblImageTemp.left = new FormAttachment(45);
		fd_lblImageTemp.right = new FormAttachment(100, -5);
		fd_lblImageTemp.top = new FormAttachment(lblSelectedFunctionAlone, 0, SWT.TOP);
		fd_lblImageTemp.bottom = new FormAttachment(100);
		lblImageTemp.setLayoutData(fd_lblImageTemp);
		
		Group grpParameters = new Group(container, SWT.NONE);
		grpParameters.setText("Parameters");
		grpParameters.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		grpParameters.setLayout(new GridLayout(8, false));
		FormData fd_grpParameters = new FormData();
		fd_grpParameters.right = new FormAttachment(44);
		fd_grpParameters.top = new FormAttachment(lblFunctions, 6);
		fd_grpParameters.left = new FormAttachment(0, 10);
		grpParameters.setLayoutData(fd_grpParameters);
		
		lblPar1 = new Label(grpParameters, SWT.NONE);
		lblPar1.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		lblPar1.setText("Tl        ");
		
		txtPar1 = new Text(grpParameters, SWT.BORDER);
		txtPar1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpParameters, SWT.NONE);
		
		lblPar2 = new Label(grpParameters, SWT.NONE);
		lblPar2.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		lblPar2.setText("Th        ");
		
		txtPar2 = new Text(grpParameters, SWT.BORDER);
		txtPar2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpParameters, SWT.NONE);
		
		lblPar3 = new Label(grpParameters, SWT.NONE);
		lblPar3.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		lblPar3.setText("Ha        ");
		
		txtPar3 = new Text(grpParameters, SWT.BORDER);
		txtPar3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblPar4 = new Label(grpParameters, SWT.NONE);
		lblPar4.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		lblPar4.setText("Hl        ");
		
		txtPar4 = new Text(grpParameters, SWT.BORDER);
		txtPar4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpParameters, SWT.NONE);
		
		lblPar5 = new Label(grpParameters, SWT.NONE);
		lblPar5.setText("Hh        ");
		lblPar5.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		
		txtPar5 = new Text(grpParameters, SWT.BORDER);
		txtPar5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpParameters, SWT.NONE);
		
		lblPar6 = new Label(grpParameters, SWT.NONE);
		lblPar6.setText("          ");
		lblPar6.setVisible(false);
		lblPar6.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		
		txtPar6 = new Text(grpParameters, SWT.BORDER);
		txtPar6.setVisible(false);
		txtPar6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblPar7 = new Label(grpParameters, SWT.NONE);
		lblPar7.setText("          ");
		lblPar7.setVisible(false);
		lblPar7.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		
		txtPar7 = new Text(grpParameters, SWT.BORDER);
		txtPar7.setVisible(false);
		txtPar7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpAdjustment = new Group(container, SWT.NONE);
		grpAdjustment.setLayout(new FormLayout());
		grpAdjustment.setText("Auto Adjustment");
		grpAdjustment.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		FormData fd_grpAdjustment = new FormData();
		fd_grpAdjustment.right = new FormAttachment(44);
		fd_grpAdjustment.top = new FormAttachment(grpParameters, 6);
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		fd_grpAdjustment.left = new FormAttachment(0, 10);
		grpAdjustment.setLayoutData(fd_grpAdjustment);
		
		Label label = new Label(grpAdjustment, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0,5);
		fd_label.left = new FormAttachment(0);
		label.setLayoutData(fd_label);
		label.setText("Adjustment");
		
		Button btnReAdjust = new Button(grpAdjustment, SWT.NONE);
		btnReAdjust.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		FormData fd_btnReAdjust = new FormData();
		fd_btnReAdjust.right = new FormAttachment(100);
		fd_btnReAdjust.top = new FormAttachment(0);
		btnReAdjust.setLayoutData(fd_btnReAdjust);
		btnReAdjust.setText("Readjust");

		/*Text */txtAdjust = new Text(grpAdjustment, SWT.BORDER);
		FormData fd_txtAdjust = new FormData();
		fd_txtAdjust.left = new FormAttachment(label,10);
		fd_txtAdjust.right = new FormAttachment(btnReAdjust, -5);
		fd_txtAdjust.top = new FormAttachment(0, 5);
		txtAdjust.setLayoutData(fd_txtAdjust);
		
		Button btnSet = new Button(grpAdjustment, SWT.NONE);
		btnSet.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		FormData fd_btnSet = new FormData();
		fd_btnSet.left = new FormAttachment(btnReAdjust, 0, SWT.LEFT);
		fd_btnSet.right = new FormAttachment(100);
		fd_btnSet.top = new FormAttachment(btnReAdjust, 5);
		btnSet.setLayoutData(fd_btnSet);
		btnSet.setText("Set");
		
		Button btnBack = new Button(grpAdjustment, SWT.NONE);
		btnBack.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		FormData fd_btnBack = new FormData();
		fd_btnBack.left = new FormAttachment(btnSet, -60, SWT.LEFT);
		fd_btnBack.right = new FormAttachment(btnSet, -1);
		fd_btnBack.top = new FormAttachment(btnSet, 0, SWT.TOP);
		btnBack.setLayoutData(fd_btnBack);
		btnBack.setText("Back");
		
		final Button chkTmin = new Button(container, SWT.CHECK);
		chkTmin.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		FormData fd_chkTmin = new FormData();
		fd_chkTmin.left = new FormAttachment(0, 15);
		fd_chkTmin.top = new FormAttachment(grpAdjustment, 15);
		chkTmin.setLayoutData(fd_chkTmin);
		
		Group grpExtremeValues = new Group(container, SWT.NONE);
		grpExtremeValues.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		grpExtremeValues.setText("   Additional values");
		grpExtremeValues.setLayout(new GridLayout(5, false));
	    FormData fd_grpExtremeValues = new FormData();
	    fd_grpExtremeValues.left = new FormAttachment(0, 10);
	    fd_grpExtremeValues.right = new FormAttachment(lblImageTemp, -6);
	    fd_grpExtremeValues.top = new FormAttachment(grpAdjustment, 15);
	    grpExtremeValues.setLayoutData(fd_grpExtremeValues);
	    
	    Label l1 = new Label(grpExtremeValues, SWT.NONE);
	    l1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		l1.setText("Temp.");
		
		txtTminTempOne= new Text(grpExtremeValues, SWT.BORDER);
		txtTminTempOne.setEditable(false);
		
		Label l2 = new Label(grpExtremeValues, SWT.NONE);
	    l2.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		l2.setText("Value");
		
		txtTminValueOne = new Text(grpExtremeValues, SWT.BORDER);
		txtTminValueOne.setEditable(false);
		
		Button btnInclude = new Button(grpExtremeValues, SWT.PUSH);
		l2.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NONE));
		btnInclude.setText("Include values");
		
		chkLimitsOne = new Button(container, SWT.CHECK);
		chkLimitsOne.setSelection(true);
		FormData fd_chkLimits = new FormData();
		fd_chkLimits.left = new FormAttachment(grpExtremeValues, 0, SWT.LEFT);
		fd_chkLimits.top = new FormAttachment(grpExtremeValues, 20);
		chkLimitsOne.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		chkLimitsOne.setText("Limits");
		chkLimitsOne.setLayoutData(fd_chkLimits);
		
		lblStageSelected = new Label(container, SWT.NONE);
		lblStageSelected.setText("      Stage selected:");
		lblStageSelected.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		FormData fd_lblStageSelected = new FormData();
		fd_lblStageSelected.top = new FormAttachment(lblSelectedAlgo, 6);
		fd_lblStageSelected.left = new FormAttachment(lblSelectedAlgo, 0, SWT.LEFT);
		lblStageSelected.setLayoutData(fd_lblStageSelected);
		
		textStageSelected = new Text(container, SWT.NONE);
		textStageSelected.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		textStageSelected.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		FormData fd_textStageSelected = new FormData();
		fd_textStageSelected.right = new FormAttachment(textAlgoSelected, 0, SWT.RIGHT);
		fd_textStageSelected.top = new FormAttachment(lblSelectedAlgo, 6);
		fd_textStageSelected.left = new FormAttachment(textAlgoSelected, 0, SWT.LEFT);
		textStageSelected.setLayoutData(fd_textStageSelected);
		
		btnReAdjust.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AutoDissemination.randomParameters();
			}
		});
		btnSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AutoDissemination.setParameters();
			}
		});
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AutoDissemination.backParameters();
			}
		});
		
		btnInclude.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtTminTempOne.getText().equalsIgnoreCase("") && !txtTminValueOne.getText().equalsIgnoreCase(""))
				AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
						txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection());
			}
		});
		
		
		chkTmin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(chkTmin.getSelection()){
					txtTminTempOne.setEditable(true);
					txtTminValueOne.setEditable(true);
				}else{
					txtTminTempOne.setEditable(false);
					txtTminValueOne.setEditable(false);
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection());
				}
			}
		});
		
		txtPar1.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13 || e.keyCode == 16777296)
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection()/*, chkTmax.getSelection()*/);
			}
		});
		txtPar2.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13 || e.keyCode == 16777296)
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection()/*, chkTmax.getSelection()*/);
			}
		});
		txtPar3.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13 || e.keyCode == 16777296)
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection()/*, chkTmax.getSelection()*/);
			}
		});
		txtPar4.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13 || e.keyCode == 16777296)
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection()/*, chkTmax.getSelection()*/);
			}
		});
		txtPar5.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13 || e.keyCode == 16777296)
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection()/*, chkTmax.getSelection()*/);
			}
		});
		txtPar6.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13 || e.keyCode == 16777296)
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection()/*, chkTmax.getSelection()*/);
			}
		});
		txtPar7.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13 || e.keyCode == 16777296)
					AutoDissemination.spinnerListener(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(),
							txtPar3.getText(), txtPar4.getText(), txtPar5.getText(), txtPar6.getText(), txtPar7.getText(), chkTmin.getSelection()/*, chkTmax.getSelection()*/);
			}
		});
	}
	
	public static String[] getExtremValuesOne(){
		return new String[]{txtTminTempOne.getText(), txtTminValueOne.getText()};
	}

	public static String getLimitsOne(){
		if(chkLimitsOne.getSelection())
			return "yes";
		else
			return "no";
	}
	
	/** Metodo que muestra los nombres de los parametros correspondientes, segun el numero del modelo **/
	public static void showParametersLabel(int model){
		switch (model) {
			case 1:
				lblPar1.setText("Tl");
				lblPar2.setText("Th");
				lblPar3.setText("Ha");
				lblPar4.setText("Hl");
				lblPar5.setText("Hh");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 2:
				lblPar1.setText("To");
				lblPar2.setText("Tl");
				lblPar3.setText("Th");
				lblPar4.setText("Ha");
				lblPar5.setText("Hl");
				lblPar6.setText("Hh");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(true);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(true);
				txtPar7.setVisible(false);
			break;
			case 3:
				lblPar1.setText("p");
				lblPar2.setText("To");
				lblPar3.setText("Tl");
				lblPar4.setText("Th");
				lblPar5.setText("Ha");
				lblPar6.setText("Hl");
				lblPar7.setText("Hh");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(true);
				lblPar7.setVisible(true);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(true);
				txtPar7.setVisible(true);
			break;
			case 4:
				lblPar1.setText("p");
				lblPar2.setText("To");
				lblPar3.setText("Ha");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 5:
				lblPar1.setText("p");
				lblPar2.setText("To");
				lblPar3.setText("Tl");
				lblPar4.setText("Ha");
				lblPar5.setText("Hl");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 6:
				lblPar1.setText("p");
				lblPar2.setText("To");
				lblPar3.setText("Th");
				lblPar4.setText("Ha");
				lblPar5.setText("Hh");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 7:
				lblPar1.setText("To");
				lblPar2.setText("Ha");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 8:
				lblPar1.setText("To");
				lblPar2.setText("Tl");
				lblPar3.setText("Ha");
				lblPar4.setText("Hl");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 9:
				lblPar1.setText("To");
				lblPar2.setText("Th");
				lblPar3.setText("Ha");
				lblPar4.setText("Hh");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 10:
				lblPar1.setText("p");
				lblPar2.setText("Tl");
				lblPar3.setText("Ha");
				lblPar4.setText("Hl");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 11:
				lblPar1.setText("p");
				lblPar2.setText("Tl");
				lblPar3.setText("Th");
				lblPar4.setText("Ha");
				lblPar5.setText("Hl");
				lblPar6.setText("Hh");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(true);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(true);
				txtPar7.setVisible(false);
			break;
			case 12:
				lblPar1.setText("Tl");
				lblPar2.setText("Th");
				lblPar3.setText("Ha");
				lblPar4.setText("Hl");
				lblPar5.setText("Hh");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 13:
				lblPar1.setText("p");
				lblPar2.setText("Tl");
				lblPar3.setText("Ha");
				lblPar4.setText("Hl");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 14:
				lblPar1.setText("p");
				lblPar2.setText("Th");
				lblPar3.setText("Ha");
				lblPar4.setText("Hh");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 15:/**Deva 1**/
				lblPar1.setText("Tmin");
				lblPar2.setText("b");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 16:/**Deva 2**/
				lblPar1.setText("b1");
				lblPar2.setText("b2");
				lblPar3.setText("b3");
				lblPar4.setText("b4");
				lblPar5.setText("b5");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 17:/**Logan 1**/
				lblPar1.setText("Y");
				lblPar2.setText("Tmax");
				lblPar3.setText("p");
				lblPar4.setText("v");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 18:/**Logan 2**/
				lblPar1.setText("alfa");
				lblPar2.setText("k");
				lblPar3.setText("Tmax");
				lblPar4.setText("p");
				lblPar5.setText("v");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 19:/**Briere 1**/
				lblPar1.setText("aa");
				lblPar2.setText("To");
				lblPar3.setText("Tmax");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 20:/**Briere 2**/
				lblPar1.setText("aa");
				lblPar2.setText("To");
				lblPar3.setText("Tmax");
				lblPar4.setText("d");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 21:/**Stinner 1**/
				lblPar1.setText("Rmax");
				lblPar2.setText("Topc");
				lblPar3.setText("k1");
				lblPar4.setText("k2");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 22:/**Hilbert y Logan**/
				lblPar1.setText("d");
				lblPar2.setText("Y");
				lblPar3.setText("Tmax");
				lblPar4.setText("v");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 23:/**Lactin**/
				lblPar1.setText("Tl");
				lblPar2.setText("p");
				lblPar3.setText("dt");
				lblPar4.setText("L");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 24:/** linear **/
				lblPar1.setText("Inter");
				lblPar2.setText("Slop");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 25:/** exponential simple **/
				lblPar1.setText("b1");
				lblPar2.setText("b2");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 26:/** Tb Model (Logan) **/
				lblPar1.setText("sy");
				lblPar2.setText("b");
				lblPar3.setText("Tb");
				lblPar4.setText("DTb");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 27:/** Exponential Model (Logan) **/
				lblPar1.setText("sy");
				lblPar2.setText("b");
				lblPar3.setText("Tb");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 28:/** Exponential Tb (Logan) **/
				lblPar1.setText("b");
				lblPar2.setText("Tmin");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 29:/** Square root model of Ratkowsky **/
				lblPar1.setText("b");
				lblPar2.setText("Tb");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 30:/**Davidson **/ 
				lblPar1.setText("k");
				lblPar2.setText("a");
				lblPar3.setText("b");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 31:/** Pradham **/
				lblPar1.setText("R");
				lblPar2.setText("Tm");
				lblPar3.setText("To");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 32:/** Angilletta Jr. **/
				lblPar1.setText("a");
				lblPar2.setText("b");
				lblPar3.setText("c");
				lblPar4.setText("d");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 33:/** Stinner 2 **/
				lblPar1.setText("Rmax");
				lblPar2.setText("k1");
				lblPar3.setText("k2");
				lblPar4.setText("Topc");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 34:/** Hilbert **/
				lblPar1.setText("Tb");
				lblPar2.setText("Tmax");
				lblPar3.setText("d");
				lblPar4.setText("Y");
				lblPar5.setText("v");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 35:/** Lactin 2 **/
				lblPar1.setText("Tl");
				lblPar2.setText("p");
				lblPar3.setText("dt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 36:/** Anlytis-1 **/
				lblPar1.setText("P");
				lblPar2.setText("Tmax");
				lblPar3.setText("Tmin");
				lblPar4.setText("n");
				lblPar5.setText("m");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 37:/** Anlytis-2 **/
				lblPar1.setText("P");
				lblPar2.setText("Tmax");
				lblPar3.setText("Tmin");
				lblPar4.setText("n");
				lblPar5.setText("m");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 38:/** Anlytis-3 **/
				lblPar1.setText("a");
				lblPar2.setText("Tmax");
				lblPar3.setText("Tmin");
				lblPar4.setText("n");
				lblPar5.setText("m");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 39:/** Allahyari **/
				lblPar1.setText("P");
				lblPar2.setText("Tmax");
				lblPar3.setText("Tmin");
				lblPar4.setText("n");
				lblPar5.setText("m");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 40:/** Briere 3 **/
				lblPar1.setText("aa");
				lblPar2.setText("To");
				lblPar3.setText("Tmax");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 41:/** Briere 4 **/
				lblPar1.setText("aa");
				lblPar2.setText("To");
				lblPar3.setText("Tmax");
				lblPar4.setText("n");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 42:/** Kontodimas-1 **/
				lblPar1.setText("aa");
				lblPar2.setText("Tmin");
				lblPar3.setText("Tmax");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 43:/** Kontodimas-2 **/
				lblPar1.setText("Dmin");
				lblPar2.setText("Topt");
				lblPar3.setText("K");
				lblPar4.setText("lmda");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 44:/** Kontodimas-3 **/
				lblPar1.setText("a1");
				lblPar2.setText("b1");
				lblPar3.setText("c1");
				lblPar4.setText("d1");
				lblPar5.setText("f1");
				lblPar6.setText("g1");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(true);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(true);
				txtPar7.setVisible(false);
				break;
			case 45:/** Ratkowsky 2 **/
				lblPar1.setText("aa");
				lblPar2.setText("Tmin");
				lblPar3.setText("Tmax");
				lblPar4.setText("b");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 46:/** Janish-1 **/
				lblPar1.setText("Dmin");
				lblPar2.setText("Topt");
				lblPar3.setText("K");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 47:/** Janish-2 **/
				lblPar1.setText("c");
				lblPar2.setText("a");
				lblPar3.setText("b");
				lblPar4.setText("Tm");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 48:/** Tanigoshi **/
				lblPar1.setText("a0");
				lblPar2.setText("a1");
				lblPar3.setText("a2");
				lblPar4.setText("a3");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 49:/** Wang-Lan-Ding **/
				lblPar1.setText("k");
				lblPar2.setText("a");
				lblPar3.setText("b");
				lblPar4.setText("c");
				lblPar5.setText("Tmin");
				lblPar6.setText("Tmax");
				lblPar7.setText("r");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(true);
				lblPar7.setVisible(true);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(true);
				txtPar7.setVisible(true);
				break;
			case 50:/** Stinner-3**/
				lblPar1.setText("c1");
				lblPar2.setText("k1");
				lblPar3.setText("k2");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
			break;
			case 51:/** Stinner-4 **/
				lblPar1.setText("c1");
				lblPar2.setText("c2");
				lblPar3.setText("k1");
				lblPar4.setText("k2");
				lblPar5.setText("To");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 52:/** Logan-3 **/
				lblPar1.setText("sy");
				lblPar2.setText("b");
				lblPar3.setText("Tmin");
				lblPar4.setText("Tmax");
				lblPar5.setText("DTb");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 53:/** Logan-4 **/
				lblPar1.setText("alph");
				lblPar2.setText("k");
				lblPar3.setText("b");
				lblPar4.setText("Tmin");
				lblPar5.setText("Tmax");
				lblPar6.setText("Dt");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(true);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(true);
				txtPar7.setVisible(false);
				break;
			case 54:/** Logan-5 **/
				lblPar1.setText("alph");
				lblPar2.setText("k");
				lblPar3.setText("b");
				lblPar4.setText("Tmax");
				lblPar5.setText("Dt");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 55:/** Hilber y logan 2 **/
				lblPar1.setText("trid");
				lblPar2.setText("D");
				lblPar3.setText("Tmax");
				lblPar4.setText("Dt");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 56:/** Hilber y logan 3**/
				lblPar1.setText("trid");
				lblPar2.setText("Tmax");
				lblPar3.setText("Tmin");
				lblPar4.setText("D");
				lblPar5.setText("Dt");
				lblPar6.setText("Smin");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(true);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(true);
				txtPar7.setVisible(false);
				break;
			case 57:/** Taylor **/
				lblPar1.setText("rm");
				lblPar2.setText("Topt");
				lblPar3.setText("Troh");
				lblPar4.setText("Smin");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 58:/** Lactin 3 **/
				lblPar1.setText("p");
				lblPar2.setText("Tl");
				lblPar3.setText("dt");
				lblPar4.setText("lamb");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
			case 59:/** Sigmoid or logistic **/
				lblPar1.setText("c1");
				lblPar2.setText("a");
				lblPar3.setText("b");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 60:/** MAIZSIM **/
				lblPar1.setText("Rmax");
				lblPar2.setText("Tceil");
				lblPar3.setText("Topc");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 61:/**  Enzymatic Response **/
				lblPar1.setText("alpha");
				lblPar2.setText("To");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 62:/**  beta 1**/ //k,alpha,betas,Tb, Tc
				lblPar1.setText("k");
				lblPar2.setText("alpha");
				lblPar3.setText("betas");
				lblPar4.setText("Tb");
				lblPar5.setText("Tc");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 63:/**  Wang et Engel **/ //Tmin,Tmax,Topt
				lblPar1.setText("Tmin");
				lblPar2.setText("Tmax");
				lblPar3.setText("Topt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 64:/**  Richards **/ //Yasym,k,Tm,v
				lblPar1.setText("Yasym");
				lblPar2.setText("k");
				lblPar3.setText("Tm");
				lblPar4.setText("v");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 65:/**  Gompertz **/ //Yasym,k,Tm
				lblPar1.setText("Yasym");
				lblPar2.setText("k");
				lblPar3.setText("Tm");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 66:/**  Beta 2 **/ //Rmax,Tmax,Topt
				lblPar1.setText("Rmax");
				lblPar2.setText("Tmax");
				lblPar3.setText("Topt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 67:/**  Q10 function **/ //Q10,Tref
				lblPar1.setText("Q10");
				lblPar2.setText("Tref");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 68:/**  Ratkowsky 3 **/ //Tmin,Tref
				lblPar1.setText("Tmin");
				lblPar2.setText("Tref");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 69:/**  Beta 3 **/ //Rmax,Rmax,Tmax
				lblPar1.setText("Rmax");
				lblPar2.setText("Rmax");
				lblPar3.setText("Tmax");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 70:/**  Bell curve **/ //Yasym,a,b,Topt
				lblPar1.setText("Yasym");
				lblPar2.setText("a");
				lblPar3.setText("b");
				lblPar4.setText("Topt");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 71:/**  Gaussian function **/ //Yasym,b,Topt
				lblPar1.setText("Yasym");
				lblPar2.setText("b");
				lblPar3.setText("Topt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 72:/**  beta 4**/ //Rmax,Tmin,Tmax,Topt
				lblPar1.setText("Rmax");
				lblPar2.setText("Tmin");
				lblPar3.setText("Tmax");
				lblPar4.setText("Topt");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 73:/**  Expo first order plus logistic **/ //Yo,k,b
				lblPar1.setText("Yo");
				lblPar2.setText("k");
				lblPar3.setText("b");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 74:/**  beta 5**/ //Yb,Rmax,Tmax,Topt,Tmin
				lblPar1.setText("Yb");
				lblPar2.setText("Rmax");
				lblPar3.setText("Tmax");
				lblPar4.setText("Topt");
				lblPar5.setText("Tmin");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(true);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(true);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 75:/**  beta 6**/ //Rmax,Tmax
				lblPar1.setText("Rmax");
				lblPar2.setText("Tmax");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 76:/**  beta 7**/ //Rmax,Tmax
				lblPar1.setText("Rmax");
				lblPar2.setText("Tmax");
				lblPar3.setText("");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(false);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(false);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 77:/**  beta 8**/ //Rmax,Tmax,Topt
				lblPar1.setText("Rmax");
				lblPar2.setText("Tmax");
				lblPar3.setText("Topt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 78:/**  Modified exponential **/ //a,b,Topt
				lblPar1.setText("a");
				lblPar2.setText("b");
				lblPar3.setText("Topt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 79:/** Lorentzian 3-parameter **/ //a,b,Topt
				lblPar1.setText("a");
				lblPar2.setText("b");
				lblPar3.setText("Topt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 80:/**  Lorentzian 4-parameter **/ //Yopt,a,b,Topt
				lblPar1.setText("Yopt");
				lblPar2.setText("a");
				lblPar3.setText("b");
				lblPar4.setText("Topt");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 81:/** Log normal 3-parameter **/ //a,b,Topt
				lblPar1.setText("a");
				lblPar2.setText("b");
				lblPar3.setText("Topt");
				lblPar4.setText("");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(false);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(false);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			case 82:/**  Pseudo-voigt 4 parameter **/ //a,b,k,Topt
				lblPar1.setText("a");
				lblPar2.setText("b");
				lblPar3.setText("k");
				lblPar4.setText("Topt");
				lblPar5.setText("");
				lblPar6.setText("");
				lblPar7.setText("");
				
				lblPar3.setVisible(true);
				lblPar4.setVisible(true);
				lblPar5.setVisible(false);
				lblPar6.setVisible(false);
				lblPar7.setVisible(false);
				
				txtPar3.setVisible(true);
				txtPar4.setVisible(true);
				txtPar5.setVisible(false);
				txtPar6.setVisible(false);
				txtPar7.setVisible(false);
				break;
				
			default:
					break;
	
		}
	}
	
	@Override
	public IWizardPage getNextPage() {
		AutoDissemination.setModelSelectedOne(AutoDissemination.getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(0)), txtPar1.getText(), txtPar2.getText(), txtPar3.getText(), txtPar4.getText(),
				txtPar5.getText(), txtPar6.getText(), txtPar7.getText());
		return super.getWizard().getPage("OneModelWizardPage2");
	}

	@Override
	public IWizardPage getPreviousPage() {
		return super.getPreviousPage();
	}
	
	
}
