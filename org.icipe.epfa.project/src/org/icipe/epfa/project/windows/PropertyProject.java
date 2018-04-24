package org.icipe.epfa.project.windows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.icipe.epfa.classUtils.ArrayConvertions;

public class PropertyProject 
{
	Display display = Display.getDefault();
	protected Object result;
	protected Shell shlProperty;
	private Label txtName, txtPath/*, txtSpeciesName*/, txtRate ;
	private Text txtSpeciesName, txtAuthor;
	
	private Button btnModifyAdults, btnModifyInmatures;
	String[] inmArray, madArray;

	public Object open() {
		shlProperty.open();
		shlProperty.layout();
		display = Display.getDefault();
		while (!shlProperty.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContents() {
		shlProperty = new Shell(display.getActiveShell(), SWT.MIN | SWT.PRIMARY_MODAL | SWT.BORDER);
		shlProperty.setSize(520, 249);
		shlProperty.setText("Project Properties");
		
		Label lblName = new Label(shlProperty, SWT.NONE);
		lblName.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblName.setBounds(20, 30, 49, 20);
		lblName.setText("Name");
		
		Label lblLocation = new Label(shlProperty, SWT.NONE);
		lblLocation.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblLocation.setBounds(20, 50, 55, 20);
		lblLocation.setText("Location");
		
		Label lblRate = new Label(shlProperty, SWT.NONE);
		lblRate.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblRate.setBounds(19, 160, 49, 20);
		lblRate.setText("");
		
		txtName = new Label(shlProperty, SWT.NONE);
		txtName.setFont(SWTResourceManager.getFont("Century Schoolbook", 10, SWT.NORMAL));
		txtName.setBounds(110, 30, 394, 20);
		
		txtPath = new Label(shlProperty, SWT.NONE);
		txtPath.setFont(SWTResourceManager.getFont("Century Schoolbook", 10, SWT.NORMAL));
		txtPath.setBounds(110, 50, 394, 20);
/*/*			
		txtRate = new Label(shlProperty, SWT.NONE);
		txtRate.setFont(SWTResourceManager.getFont("Century Schoolbook", 10, SWT.NORMAL));
		txtRate.setBounds(109, 160, 395, 20);
		
		btnModifyRate = new Button(shlProperty, SWT.CHECK);
		btnModifyRate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnModifyRate.getSelection())
					txtRate.setEnabled(true);
				else
					txtRate.setEnabled(false);
			}
		});
		btnModifyRate.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		btnModifyRate.setBounds(462, 191, 65, 16);
		btnModifyRate.setText("Modify");*/
		
		Group grpLifestages = new Group(shlProperty, SWT.NONE);
		grpLifestages.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		grpLifestages.setText("Fungi-Information");
		grpLifestages.setBounds(20, 70, 484, 80);
		
		Label lblLifestages = new Label(grpLifestages, SWT.NONE);
		lblLifestages.setBounds(5, 21, 70, 20);
		lblLifestages.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblLifestages.setText("Fungi isolate");
		
		txtSpeciesName = new Text(grpLifestages, SWT.NONE);
		txtSpeciesName.setEnabled(false);
		txtSpeciesName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSpeciesName.setFont(SWTResourceManager.getFont("Century Schoolbook", 10, SWT.NORMAL));
		txtSpeciesName.setBounds(90, 21, 310, 20);
		
		btnModifyInmatures = new Button(grpLifestages, SWT.CHECK);
		btnModifyInmatures.setBounds(410, 21, 65, 16);
		btnModifyInmatures.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	/*			if(btnModifyInmatures.getSelection()){
					txtSpeciesName.setEnabled(true);
					txtSpeciesName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					inmArray = ArrayConvertions.StringtoArray(txtSpeciesName.getText(), ",");
				}else{
					txtSpeciesName.setEnabled(false);
					txtSpeciesName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				}*/
			}
		});
		btnModifyInmatures.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		btnModifyInmatures.setText("Modify");
		
		Label lblAdults = new Label(grpLifestages, SWT.NONE);
		lblAdults.setText("Author");
		lblAdults.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblAdults.setBounds(5, 47, 70, 20);
		
		txtAuthor = new Text(grpLifestages, SWT.NONE);
		txtAuthor.setEnabled(false);
		txtAuthor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAuthor.setFont(SWTResourceManager.getFont("Century Schoolbook", 10, SWT.NORMAL));
		txtAuthor.setBounds(90, 47, 310, 20);
		
		btnModifyAdults = new Button(grpLifestages, SWT.CHECK);
		btnModifyAdults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
/*				if(btnModifyAdults.getSelection()){
					txtAuthor.setEnabled(true);
					txtAuthor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					madArray = ArrayConvertions.StringtoArray(txtAuthor.getText(), ",");
				}else{
					txtAuthor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
					txtAuthor.setEnabled(false);
				}*/
			}
		});
		btnModifyAdults.setText("Modify");
		btnModifyAdults.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		btnModifyAdults.setBounds(410, 47, 65, 16);
		
		Button btnAccept = new Button(shlProperty, SWT.NONE);
		btnAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(modificarEPFA())
					shlProperty.dispose();
			}
		});
		btnAccept.setFont(SWTResourceManager.getFont("Comic Sans MS", 9, SWT.NORMAL));
		btnAccept.setBounds(409, 184, 95, 23);
		btnAccept.setText("Accept");
		
	}
	
	Properties prop = new Properties();
	public void showProperties(){
		File file = new File(ViewProjectsUI.pathProject + File.separator + ViewProjectsUI.nameProject + ".epfa");
		
		try {
			prop.load(new FileInputStream(file));
			
			txtName.setText(prop.getProperty("ProjectName"));
			txtPath.setText(Platform.getLocation().toString());
			txtSpeciesName.setText(prop.getProperty("SpeciesName"));
			txtAuthor.setText(prop.getProperty("Author"));
//			txtRate.setText(prop.getProperty("Ratio"));
			
			prop.clear();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean modificarEPFA(){
		File file = null;
		/*try {
			prop.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		
		/** modificando los estadios **/
		if(btnModifyInmatures.getSelection()){
			String[] imArrayMod = ArrayConvertions.StringtoArray(txtSpeciesName.getText(), ",");
			String[] madArrayMod = ArrayConvertions.StringtoArray(txtAuthor.getText(), ",");
			
			if(imArrayMod.length != inmArray.length){
				MessageDialog.openError(shlProperty, "Error", "You must to enter the same life-stage number as before you did it");
				return false;
			}
			
			
			try {
				file = new File(ViewProjectsUI.pathProject + File.separator + ViewProjectsUI.nameProject + ".epfa");
				prop.load(new FileInputStream(file));
				prop.setProperty("SpeciesName", txtSpeciesName.getText());
				prop.store(new FileOutputStream(file), "EPFA");
				prop.clear();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			file = new File(ViewProjectsUI.pathProject + File.separator + "Progress.epfa");
			
	        
	        try {
	        	BufferedReader in = new BufferedReader(new FileReader(file));
	        	String str, str2="";
	        	int c=0;
				while ((str = in.readLine()) != null) {
					if(c>0 && c < (imArrayMod.length+1))
						str = str.replace(str.split("\t")[0], imArrayMod[c-1]);
					
					if(c == (imArrayMod.length + madArrayMod.length) )
						str2+=str;
					else
						str2+=str+"\r\n";
					c++;
					
				}
				in.close();
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(str2);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
	        
		}
		
		if(btnModifyAdults.getSelection()){
			String[] imArrayMod = ArrayConvertions.StringtoArray(txtSpeciesName.getText(), ",");
			String[] madArrayMod = ArrayConvertions.StringtoArray(txtAuthor.getText(), ",");
			
			if(madArrayMod.length != madArray.length){
				MessageDialog.openError(shlProperty, "Error", "You must to enter the same life-stage number as before you did it");
				return false;
			}
			
			try {
				file = new File(ViewProjectsUI.pathProject + File.separator + ViewProjectsUI.nameProject + ".epfa");
				prop.load(new FileInputStream(file));
				prop.setProperty("Author", txtAuthor.getText());
				prop.store(new FileOutputStream(file), "EPFA");
				prop.clear();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			file = new File(ViewProjectsUI.pathProject + File.separator + "Progress.epfa");
			
			try {
	        	BufferedReader in = new BufferedReader(new FileReader(file));
	        	String str, str2="";
	        	int c=0, m=0;
				while ((str = in.readLine()) != null) {
					if(c>imArrayMod.length && c < (imArrayMod.length + madArrayMod.length+1)){
						str = str.replace(str.split("\t")[0], madArrayMod[m]);
						m++;
					}
					if(c == (imArrayMod.length + madArrayMod.length) )
						str2+=str;
					else
						str2+=str+"\r\n";
					c++;
					
				}
				in.close();
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(str2);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
			
		}
		
		return true;
	}
	

}
