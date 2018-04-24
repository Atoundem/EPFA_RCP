package org.icipe.epfa.modeldesigner;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.icipe.epfa.modeldesigner.wizards.DevRateWizardDialog;
import org.icipe.epfa.modeldesigner.wizards.MainPageWizardPage;
import org.icipe.epfa.classUtils.ArrayConvertions;
import org.icipe.epfa.project.windows.ViewProjectsUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class MainActionRate {
	
	public static String nameProject, pathProj;
	


	public static void launchWizard(){
		if(ViewProjectsUI.nameProject == null){
			MessageDialog.openError(new Shell(), "Error", "You must select a project");
			return;
		}else{
			nameProject = ViewProjectsUI.nameProject;
			pathProj = ViewProjectsUI.pathProject.replace('\\', '/');
		}
		
		WizardDialog w = new WizardDialog(new Shell(), new DevRateWizardDialog());
		w.setPageSize(900, 555);
		w.create();
		
		MainPageWizardPage.txtSelectedProject.setText(nameProject);
/*		BufferedReader br;
		int inmaturesSize = ArrayConvertions.StringtoArray(ViewProjectsUI.getLifeStages()[0],",").length;
	
		try {
			br = new BufferedReader(new FileReader(pathProj + File.separator + "Progress.ilcym"));
			String str;
	        int i=0;
	        while ((str = br.readLine()) != null) {
	            if(i>0 && i <= inmaturesSize){
	            	if(str.split("\t")[1].equalsIgnoreCase("true")){
	            		MainPageWizardPage.stageArrayButtons[i-1].setText(str.split("\t")[0].trim());
	            		MainPageWizardPage.stageArrayButtons[i-1].setVisible(true);
	            	}
	            }
	            i++;
	        }
	        br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
*/
		w.open();
		
	}

}
