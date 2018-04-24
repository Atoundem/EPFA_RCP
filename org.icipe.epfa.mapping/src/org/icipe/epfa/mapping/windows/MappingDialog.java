package org.icipe.epfa.mapping.windows;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.icipe.epfa.classUtils.EPFAUtils;
import org.icipe.epfa.connectiontor.Rserve;
import org.icipe.epfa.mapping.GenerateMap;
import org.rosuda.REngine.Rserve.RConnection;

public class MappingDialog extends Dialog {

	protected Object result;
	protected Shell shlEpfMapping;
	private Text textMinTemp;
	private Text textMaxTemp;
	private Text textCordMinX;
	private Text textCordMaxX;
	private Text textcordMinY;
	private Text textCordMaxY;
	private Text textNbSubArea;
	private Text txtOutputfile;
	private Text textFilterMinT;
	private Text textFilterMaxT;
	public static Label lblStatus;
	public static Button btnCalculate, btnTemperatureFilter;
	static String saveToR="";
	static RConnection c;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public MappingDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		
		if(!Platform.getOS().equalsIgnoreCase("macosx")){
			c = Rserve.launchRserve("",c);
			System.out.println("Launched !!!!");
		}
		createContents();
		shlEpfMapping.open();
		shlEpfMapping.layout();
		Display display = getParent().getDisplay();
		while (!shlEpfMapping.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlEpfMapping = new Shell(getParent(), getStyle());
		shlEpfMapping.setSize(610, 610);
		shlEpfMapping.setText("EPFA Mapping");
		shlEpfMapping.setLayout(new FormLayout());
		
		Composite composite = new Composite(shlEpfMapping, SWT.BORDER);
		FormData fd_composite = new FormData();
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.bottom = new FormAttachment(100, -51);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FormLayout());
		
		Group groupInputTemp = new Group(composite, SWT.NONE);
		groupInputTemp.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		groupInputTemp.setText("Climatic Data");
		groupInputTemp.setLayout(new GridLayout(2, false));
		FormData fd_groupInputTemp = new FormData();
		fd_groupInputTemp.top = new FormAttachment(0, 10);
		fd_groupInputTemp.left = new FormAttachment(0, 10);
		fd_groupInputTemp.right = new FormAttachment(100, -10);
		groupInputTemp.setLayoutData(fd_groupInputTemp);
		
		textMinTemp = new Text(groupInputTemp, SWT.BORDER);
		textMinTemp.setText("Select folder containing minimun Temperature");
		textMinTemp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnMinTemp = new Button(groupInputTemp, SWT.NONE);
		btnMinTemp.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD));
		btnMinTemp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dialogTmin = new DirectoryDialog(shlEpfMapping);
				
				dialogTmin.setText("Min Temperature");
				dialogTmin.setMessage("Select the directory containing Minimun temperature");
				
				String pathDir = dialogTmin.open();
				
				if(pathDir !=null)
				{
					textMinTemp.setText(pathDir);
					String[] locExtent = GenerateMap.getLocationExtent(textMinTemp.getText());
					
					textCordMinX.setText(locExtent[0]);
					textCordMaxX.setText(locExtent[1]);
					textcordMinY.setText(locExtent[2]);
					textCordMaxY.setText(locExtent[3]);
					
				}
				
			}
		});
		btnMinTemp.setText("Temp min");
		
		textMaxTemp = new Text(groupInputTemp, SWT.BORDER);
		textMaxTemp.setText("Select folder containing Maximun temperature");
		textMaxTemp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnMaxTemp = new Button(groupInputTemp, SWT.NONE);
		btnMaxTemp.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD));
		btnMaxTemp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dialogTmax = new DirectoryDialog(shlEpfMapping);
				
				dialogTmax.setText("Max Temperature");
				dialogTmax.setMessage("Select the directory containing Maximun temperature");
				
				String pathDir = dialogTmax.open();
				
				if(pathDir !=null)
				{
					textMaxTemp.setText(pathDir);
					
				}
				
			}
		});
		btnMaxTemp.setText("Temp max");
		
		Group grpCoordinates = new Group(composite, SWT.NONE);
		fd_groupInputTemp.bottom = new FormAttachment(grpCoordinates, -23);
		grpCoordinates.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		grpCoordinates.setText("Coordinate");
		grpCoordinates.setLayout(new GridLayout(6, false));
		FormData fd_grpCoordinates = new FormData();
		fd_grpCoordinates.right = new FormAttachment(100, -12);
		fd_grpCoordinates.left = new FormAttachment(0, 10);
		fd_grpCoordinates.top = new FormAttachment(0, 129);
		grpCoordinates.setLayoutData(fd_grpCoordinates);
		
		Label lblNewLabel = new Label(grpCoordinates, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 75;
		gd_lblNewLabel.heightHint = 24;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("MinLong:");
		
		textCordMinX = new Text(grpCoordinates, SWT.BORDER);
		GridData gd_textCordMinX = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textCordMinX.widthHint = 41;
		textCordMinX.setLayoutData(gd_textCordMinX);
		
		Label lblNewLabel_1 = new Label(grpCoordinates, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		lblNewLabel_1.setText(" MinLat:");
		
		textcordMinY = new Text(grpCoordinates, SWT.BORDER);
		textcordMinY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(grpCoordinates, SWT.NONE);
		btnNewButton.setText("Read from layer");
		
		Button btnNewButton_1 = new Button(grpCoordinates, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnNewButton_1.setText("Get rectangle");
		
		Label lblMaxx = new Label(grpCoordinates, SWT.NONE);
		lblMaxx.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		GridData gd_lblMaxx = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblMaxx.widthHint = 66;
		lblMaxx.setLayoutData(gd_lblMaxx);
		lblMaxx.setText("MaxLong:");
		
		textCordMaxX = new Text(grpCoordinates, SWT.BORDER);
		textCordMaxX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(grpCoordinates, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText(" MaxLat:");
		
		textCordMaxY = new Text(grpCoordinates, SWT.BORDER);
		textCordMaxY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton_2 = new Button(grpCoordinates, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if(textMinTemp.getText().equalsIgnoreCase("Select folder containing minimun Temperature") || textMinTemp.getText().equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shlEpfMapping, "Error", "You must select the Min temperature path");
	    			return;
	    		}else{
				String[] locExtent = GenerateMap.getLocationExtent(textMinTemp.getText());
				
				textCordMinX.setText(locExtent[0]);
				textCordMaxX.setText(locExtent[1]);
				textcordMinY.setText(locExtent[2]);
				textCordMaxY.setText(locExtent[3]);
	    		}
			}
		});
		btnNewButton_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnNewButton_2.setText("Read from Data");
		
		Button btnNewButton_3 = new Button(grpCoordinates, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				textCordMinX.setText(-180+"");
				textCordMaxX.setText(180+"");
				textcordMinY.setText(-90+"");
				textCordMaxY.setText(90+"");
			}
		});
		btnNewButton_3.setText("Maximum extend");
		
		Group grpTempname = new Group(composite, SWT.NONE);
		fd_grpCoordinates.bottom = new FormAttachment(grpTempname, -17);
		grpTempname.setFont(SWTResourceManager.getFont("Times New Roman CE", 10, SWT.BOLD | SWT.ITALIC));
		grpTempname.setLayout(new FormLayout());
		FormData fd_grpTempname = new FormData();
		fd_grpTempname.right = new FormAttachment(100, -12);
		fd_grpTempname.left = new FormAttachment(0, 10);
		fd_grpTempname.top = new FormAttachment(0, 256);
		grpTempname.setLayoutData(fd_grpTempname);
		
		Group grpOutput = new Group(composite, SWT.NONE);
		fd_grpTempname.bottom = new FormAttachment(grpOutput, -19);
		grpOutput.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		grpOutput.setText("Output");
		grpOutput.setLayout(new GridLayout(2, false));
		FormData fd_grpOutput = new FormData();
		fd_grpOutput.left = new FormAttachment(0, 10);
		fd_grpOutput.right = new FormAttachment(100, -10);
		fd_grpOutput.top = new FormAttachment(0, 341);
		
		btnTemperatureFilter = new Button(grpTempname, SWT.CHECK);
		btnTemperatureFilter.setSelection(true);
		btnTemperatureFilter.setEnabled(true);
		btnTemperatureFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				/*if(btnTemperatureFilter.getSelection()) 
					btnTemperatureFilter.setEnabled(true);
					//btnTemperatureFilter.setSelection(false);
				else
					btnTemperatureFilter.setEnabled(false);*/
			}
		});
		FormData fd_btnTemperatureFilter = new FormData();
		fd_btnTemperatureFilter.right = new FormAttachment(0, 152);
		fd_btnTemperatureFilter.top = new FormAttachment(0, -16);
		fd_btnTemperatureFilter.left = new FormAttachment(0, 7);
		btnTemperatureFilter.setLayoutData(fd_btnTemperatureFilter);
		btnTemperatureFilter.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 9, SWT.BOLD | SWT.ITALIC));
		btnTemperatureFilter.setText("Temperature filter ");
//		btnTemperatureFilter.setSelection(true);
		
		Label lblTmin = new Label(grpTempname, SWT.NONE);
		lblTmin.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD | SWT.ITALIC));
		FormData fd_lblTmin = new FormData();
		fd_lblTmin.bottom = new FormAttachment(0, 32);
		fd_lblTmin.top = new FormAttachment(0, 6);
		fd_lblTmin.left = new FormAttachment(0, 7);
		lblTmin.setLayoutData(fd_lblTmin);
		lblTmin.setText("Tmin : ");
		
		textFilterMinT = new Text(grpTempname, SWT.BORDER);
		fd_btnTemperatureFilter.bottom = new FormAttachment(textFilterMinT, -6);
		textFilterMinT.setText("0");
		FormData fd_textFilterMinT = new FormData();
		fd_textFilterMinT.top = new FormAttachment(0, 6);
		fd_textFilterMinT.left = new FormAttachment(lblTmin, 2);
		textFilterMinT.setLayoutData(fd_textFilterMinT);
		
		Label lblTmax = new Label(grpTempname, SWT.NONE);
		fd_textFilterMinT.right = new FormAttachment(lblTmax, -25);
		lblTmax.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD | SWT.ITALIC));
		FormData fd_lblTmax = new FormData();
		fd_lblTmax.top = new FormAttachment(textFilterMinT, 2, SWT.TOP);
		fd_lblTmax.bottom = new FormAttachment(100, -10);
		fd_lblTmax.left = new FormAttachment(0, 174);
		lblTmax.setLayoutData(fd_lblTmax);
		lblTmax.setText("Tmax : ");
		
		textFilterMaxT = new Text(grpTempname, SWT.BORDER);
		textFilterMaxT.setText("40");
		FormData fd_textFilterMaxT = new FormData();
		fd_textFilterMaxT.top = new FormAttachment(0, 6);
		fd_textFilterMaxT.right = new FormAttachment(100, -172);
		fd_textFilterMaxT.left = new FormAttachment(0, 234);
		textFilterMaxT.setLayoutData(fd_textFilterMaxT);
		grpOutput.setLayoutData(fd_grpOutput);
		
		Label lblSubAreasOf = new Label(grpOutput, SWT.NONE);
		lblSubAreasOf.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD | SWT.ITALIC));
		lblSubAreasOf.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSubAreasOf.setText("Sub areas of the map");
		
		textNbSubArea = new Text(grpOutput, SWT.BORDER);
		textNbSubArea.setText("4");
		textNbSubArea.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Button btnOutputFile = new Button(grpOutput, SWT.NONE);
		btnOutputFile.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD | SWT.ITALIC));
		btnOutputFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(shlEpfMapping, SWT.SAVE);
				dialog.setFilterExtensions(new String[]{"*.asc"});
				dialog.setFilterNames(new String[]{"ASC file (*.asc)"});
				dialog.open();
				
				txtOutputfile.setText("");
				File fTemp = new File(dialog.getFilterPath() + File.separator + dialog.getFileName());
				boolean bol = false;
				if(fTemp.exists())
					bol = MessageDialog.openConfirm(shlEpfMapping, "Mapping", dialog.getFileName() + " alredy exists. Do you want to replace it?");
				
				if(!bol)
					txtOutputfile.setText(fTemp.getPath());
				
			}
		});
		btnOutputFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnOutputFile.setText("Output File");
		
		txtOutputfile = new Text(grpOutput, SWT.BORDER);
		txtOutputfile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD | SWT.ITALIC));
		fd_grpOutput.bottom = new FormAttachment(btnCancel, -16);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlEpfMapping.dispose();
			}
		});
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(100, -12);
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		
		btnCalculate = new Button(composite, SWT.NONE);
		btnCalculate.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 10, SWT.BOLD | SWT.ITALIC));
		fd_btnCancel.left = new FormAttachment(btnCalculate, 6);
		btnCalculate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				
				// we collect input parameters
				String strOutput = (txtOutputfile.getText()).replace("\\","/");
				
//				String strOutput = "D:/TestSoftware/output/resSimul.asc";
				if(strOutput.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shlEpfMapping, "Error", "You must select the output file path");
	    			return;
	    		}
				
				String minTemptPath = (textMinTemp.getText()).replace("\\","/");
//				String minTemptPath = "D:/Icipe/MoukamRcode/datos mundo - 2000 - 10 mints/Tmin";
				if(minTemptPath.equalsIgnoreCase("Select folder containing minimun Temperature") ||minTemptPath.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shlEpfMapping, "Error", "You must select the Min temperature path");
	    			return;
	    		}
				
				String maxTempPath  = (textMaxTemp.getText()).replace("\\","/");
//				String maxTempPath  ="D:/Icipe/MoukamRcode/datos mundo - 2000 - 10 mints/Tmax";

				if(maxTempPath.equalsIgnoreCase("Select folder containing Maximun temperature") || maxTempPath.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shlEpfMapping, "Error", "You must to select the Max temperature path");
	    			return;
	    		}
				
				String strPath = new File(strOutput).getParent().replace("\\", "/");
		    	String strName = EPFAUtils.ExtractPath(new File(strOutput).getName());
				
				Boolean rbFilter =  btnTemperatureFilter.getSelection();
				
				String tmin = textFilterMinT.getText();
				if(rbFilter == true && tmin.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shlEpfMapping, "Error", "You must enter value of the Min temperature frilter");
	    			return;
	    		}
				
				
				String tmax = textFilterMaxT.getText();
				if(rbFilter == true && tmax.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shlEpfMapping, "Error", "You must enter value of the Max temperature frilter");
	    			return;
	    		}
				
//				System.out.println(btnLimitDifference.getSelection());
				System.out.println(textFilterMinT.getText());
				
				minTemptPath = minTemptPath +"/";
				maxTempPath = maxTempPath +"/";
				String nbrSubArea =  textNbSubArea.getText();
				
				String dbMinX = textCordMinX.getText();
				String dbMaxX = textCordMaxX.getText();
				String dbMinY = textcordMinY.getText();
				String dbMaxY =	textCordMaxY.getText();
				
				if(dbMinX.equalsIgnoreCase("") ||dbMaxX.equalsIgnoreCase("")|| dbMinY.equalsIgnoreCase("") ||dbMaxY.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shlEpfMapping, "Error", "You must specified coordinated value of MinX,MinY,MaxX and MaxY");
	    			return;
	    		}
				if(btnCalculate.getText().equalsIgnoreCase("  Calculate  "))
				{
				
					System.out.println("start calculate");	
						
					//lblStatus.setText(lblStatus.getText()+ "In process...Please wait");
					lblStatus.setText( "In process...Please wait");
					GenerateMap.calculateIndices( nbrSubArea, minTemptPath, maxTempPath, dbMinX, dbMaxX, dbMinY, dbMaxY, strOutput, true, tmin, tmax);
					
					if(GenerateMap.isStateMapCreation())
					{
						lblStatus.setText("Status : PROCESS END!!!");
						btnCalculate.setText("Plot Map");
						MessageDialog.openInformation(shlEpfMapping, "Infos", "Process Mapp Creation Successful!");
						//openError(shlEpfMapping, "Error", "You must specified coordinated value of MinX,MinY,MaxX and MaxY");
		    			return;
					}
						
					else
					{
						
						lblStatus.setText("Status :ERROR! Unable to create Map. Have you Model selected Phase? ");
						MessageDialog.openError(shlEpfMapping, "Error", "Unable to create Map. Have you Model selected Phase?");
		    			return;
					}
						
					
					
	//				GenerateMap.calculateIndices(strR, pathTempMin, pathTempMax, dbMinX, dbMaxX, dbMinY, dbMaxY, strOutput, rbFilter, tmin, tmax);
				}
				else if((btnCalculate.getText()).equalsIgnoreCase("Plot Map"))
				{					
					
			    	Image imge = new Image(Display.getDefault(),strPath+"/"+strName+".png");
//					lblDisplayMap.setImage(imge);
					btnCalculate.setText("  Calculate  ");

				}
				else if ((btnCalculate.getText()).equalsIgnoreCase("Close"))
					shlEpfMapping.dispose();					
							
			}
		});
		FormData fd_btnCalculate = new FormData();
		fd_btnCalculate.bottom = new FormAttachment(100, -10);
		fd_btnCalculate.left = new FormAttachment(0, 277);
		fd_btnCalculate.right = new FormAttachment(100, -113);
		btnCalculate.setLayoutData(fd_btnCalculate);
		btnCalculate.setText("  Calculate  ");
		
		Composite composite_1 = new Composite(shlEpfMapping, SWT.BORDER);
		fd_composite.right = new FormAttachment(composite_1, 0, SWT.RIGHT);
		composite_1.setLayout(new FormLayout());
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(100, -10);
		fd_composite_1.top = new FormAttachment(composite, 6);
		fd_composite_1.right = new FormAttachment(100, -10);
		fd_composite_1.left = new FormAttachment(0, 10);
		composite_1.setLayoutData(fd_composite_1);
		
		lblStatus = new Label(composite_1, SWT.NONE);
		lblStatus.setFont(SWTResourceManager.getFont("Times New Roman Baltic", 11, SWT.BOLD | SWT.ITALIC));
		FormData fd_lblStatus = new FormData();
		fd_lblStatus.bottom = new FormAttachment(0, 31);
		fd_lblStatus.right = new FormAttachment(100, -10);
		fd_lblStatus.left = new FormAttachment(0);
		fd_lblStatus.top = new FormAttachment(0);
		lblStatus.setLayoutData(fd_lblStatus);
		lblStatus.setText("Status :");
		
		

	}
}